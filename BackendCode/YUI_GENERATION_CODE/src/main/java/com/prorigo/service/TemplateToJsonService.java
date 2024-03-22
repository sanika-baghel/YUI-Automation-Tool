package com.prorigo.service;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.prorigo.dto.FormData;

/**
 * Interface for converting a template file to JSON data and writing JSON data to a file.
 */
public interface TemplateToJsonService {

	/**
	 * Converts a template file to JSON data.
	 *
	 * @param file The template file to be converted.
	 * @return A list of FormData objects representing the converted JSON data.
	 * @throws IOException If an I/O error occurs during file processing.
	 */
	public List<FormData> convertTemplateToJson(MultipartFile file) throws IOException;

	/**
	 * Writes JSON data to a file.
	 *
	 * @param json The JSON data to be written to the file.
	 * @throws IOException If an I/O error occurs while writing the file.
	 */
	public void writeToJsonFile(String json) throws IOException;
}
