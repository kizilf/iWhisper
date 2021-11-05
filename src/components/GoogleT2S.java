package components;

import com.google.api.gax.paging.Page;
import com.google.api.services.storage.model.Bucket;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
//Imports the Google Cloud client library
import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Google Cloud TextToSpeech API sample application. Example usage: mvn package
 * exec:java -Dexec.mainClass='com.example.texttospeech.QuickstartSample'
 */
public class GoogleT2S {

	private TextToSpeechClient textToSpeechClient;
	SynthesisInput input;
	VoiceSelectionParams voice;
	AudioConfig audioConfig;

	public GoogleT2S(String jsonPath) {
		/*
		 * try { authExplicit(jsonPath); dummy(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		try {
			String javaHome = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
			authExplicit(jsonPath);
			this.textToSpeechClient = TextToSpeechClient.create();
			// Build the voice request, select the language code ("en-US") and the ssml
			// voice gender
			// ("neutral")
			this.voice = VoiceSelectionParams.newBuilder()
					// .setLanguageCode("en-US")
					.setLanguageCode("tr-TR").setSsmlGender(SsmlVoiceGender.NEUTRAL).build();

			// Select the type of audio file you want returned
			this.audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setText(String text) {
		// Set the text input to be synthesized
		input = SynthesisInput.newBuilder().setText(text).build();
	}

	public ByteString getVoice() {

		// Perform the text-to-speech request on the text input with the selected voice
		// parameters and
		// audio file type
		SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

		// Get the audio contents from the response
		ByteString audioContents = response.getAudioContent();

		// Write the response to the output file.
		try (OutputStream out = new FileOutputStream("output.mp3")) {
			out.write(audioContents.toByteArray());
			System.out.println("Audio content written to file \"output.mp3\"");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return audioContents;
	}

	private void authExplicit(String jsonPath) throws IOException {
		// You can specify a credential file by providing a path to GoogleCredentials.
		// Otherwise credentials are read from the GOOGLE_APPLICATION_CREDENTIALS
		// environment variable.
		GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonPath))
				.createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
		Storage storage = (Storage) StorageOptions.newBuilder().setCredentials(credentials).build().getService();
		credentials = GoogleCredentials.getApplicationDefault();
		System.out.println("Buckets:");
		Page<com.google.cloud.storage.Bucket> buckets = storage.list();
		for (com.google.cloud.storage.Bucket bucket : buckets.iterateAll()) {
			System.out.println(bucket.toString());
		}
	}

	private void dummy() throws IOException {
		// Instantiates a client
		try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
			// Set the text input to be synthesized
			SynthesisInput input = SynthesisInput.newBuilder().setText("Hello, World!").build();

			// Build the voice request, select the language code ("en-US") and the ssml
			// voice gender
			// ("neutral")
			VoiceSelectionParams voice = VoiceSelectionParams.newBuilder().setLanguageCode("en-US")
					.setSsmlGender(SsmlVoiceGender.NEUTRAL).build();

			// Select the type of audio file you want returned
			AudioConfig audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();

			// Perform the text-to-speech request on the text input with the selected voice
			// parameters and
			// audio file type
			SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

			// Get the audio contents from the response
			ByteString audioContents = response.getAudioContent();

			// Write the response to the output file.
			try (OutputStream out = new FileOutputStream("output.mp3")) {
				out.write(audioContents.toByteArray());
				System.out.println("Audio content written to file \"output.mp3\"");
			}
		}
	}
}
