package prgrms.marco.be02marbox.aws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Component
public class S3Upload {
	private static final Logger log = LoggerFactory.getLogger(S3Upload.class);
	private static final String POSTER_DIR = "posters";
	private static final String FILE_CONVERT_EXP_MSG = "MultipartFile -> File로 전환이 실패했습니다.";
	private static final String DELETE_FILE_MSG = "파일이 삭제되었습니다.";
	private static final String DELETE_FILE_EXP_MSG = "파일이 삭제되지 못했습니다.";
	private static final String FILE_EXTENSION_SEPARATOR = ".";

	private final AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.s3.save-dir-prefix}")
	private String saveDirPrefix;

	public S3Upload(AmazonS3Client amazonS3Client) {
		this.amazonS3Client = amazonS3Client;
	}

	/**
	 * 영화 포스터를 S3에 업로드한다.
	 * @param multipartFile 포스터 첨부파일
	 * @param movieId 생성 된 영화 ID
	 * @return 파일이 생성 된 경로
	 * @throws IOException 파일 변환 실패
	 */
	public String upload(MultipartFile multipartFile, Long movieId) throws IOException {
		String fullPath = saveDirPrefix + POSTER_DIR;
		makeDirectory(fullPath);
		String extension = getFileExtension(multipartFile.getOriginalFilename());
		String fileName = generateNewFileName(fullPath, extension, movieId);

		File uploadFile = convert(multipartFile, fileName)
			.orElseThrow(() -> new IllegalArgumentException(FILE_CONVERT_EXP_MSG));
		return upload(uploadFile, fileName);
	}

	/**
	 * movieId와 환경 별 prefix를 통해 파일을 생성한다.
	 * @param uploadFile S3로 전송할 파일
	 * @return 생성된 파일 Url
	 */
	private String upload(File uploadFile, String fileName) {
		String uploadImageUrl = putS3(uploadFile, fileName);
		removeNewFile(uploadFile);
		return uploadImageUrl;
	}

	/**
	 * S3로 파일을 전송한다.
	 * @param uploadFile S3로 전송할 파일
	 * @param fileName 파일
	 * @return 생성된 파일 Url
	 */
	private String putS3(File uploadFile, String fileName) {
		amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(
			CannedAccessControlList.PublicRead));
		return amazonS3Client.getUrl(bucket, fileName).toString();
	}

	/**
	 * local에 받아온 파일은 S3에 전송 후 삭제한다.
	 * @param targetFile 로컬에 생성된 파일
	 */
	private void removeNewFile(File targetFile) {
		if (targetFile.delete()) {
			log.info(DELETE_FILE_MSG);
		} else {
			log.info(DELETE_FILE_EXP_MSG);
		}
	}

	/**
	 * MultipartFile -> File 변환.
	 * @param file 포스터 첨부파일
	 * @param fileName 파일 명
	 * @return 로컬에 생성 된 파일
	 * @throws IOException 파일 변환 실패
	 */
	private Optional<File> convert(MultipartFile file, String fileName) throws IOException {
		File convertFile = new File(fileName);
		if (convertFile.createNewFile()) {
			try (FileOutputStream fos = new FileOutputStream(convertFile)) {
				fos.write(file.getBytes());
			}
			return Optional.of(convertFile);
		}

		return Optional.empty();
	}

	/**
	 * 생성될 파일 문자열을 경로를 포함하여 만든다
	 * @param fullPath 파일 경로
	 * @param extension 확장자
	 * @param movieId 생성된 Movie ID
	 * @return 생성 될 파일 이름
	 */
	private String generateNewFileName(String fullPath, String extension, Long movieId) {
		return new StringBuilder(fullPath)
			.append("/")
			.append(movieId)
			.append(extension)
			.toString();
	}

	/**
	 * 전달받은 파일의 확장자를 추출한다.
	 * @param originalFileName 파일 명
	 * @return 확장자
	 */
	private String getFileExtension(String originalFileName) {
		String extension = "";
		if (originalFileName != null && originalFileName.contains(FILE_EXTENSION_SEPARATOR)) {
			extension = originalFileName.substring(originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR));
		}
		return extension;
	}

	/**
	 * 로컬에 저장 할 디렉터리가 존재하지 않다면 생성
	 * @param fullPath 디렉터리 경로
	 */
	private void makeDirectory(String fullPath) {
		File dir = new File(fullPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
}
