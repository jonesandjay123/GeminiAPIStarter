# Gemini API Starter

This project is a starter application using the Gemini API (specifically, the Gemini 1.5 Flash model) to generate content based on text prompts. The app provides a sample interface where users can enter prompts and view AI-generated responses.

## Features

- Use the Gemini 1.5 Flash model for text generation.
- Basic interface to input prompts and view responses.
- Example setup for safely handling API keys using `local.properties` and Gradle's Secrets Plugin.

## Prerequisites

1. **Android Studio** - Ensure you have the latest version of Android Studio installed.
2. **Android SDK** - Install SDK version 32 or higher.
3. **API Key** - Obtain an API key to access the Gemini API.

## Project Setup

### 1. Clone the Repository

If you are using version control, clone this repository using:

```bash
git clone <repository-url>
cd GeminiAPIStarter
```

### 2. Configure API Key

- Open the `local.properties` file in the project root.
- Add the following line with your API key:

    ```
    apiKey=YOUR_API_KEY
    ```

  *Replace `YOUR_API_KEY` with your actual Gemini API key.*


### 3. Secrets Gradle Plugin Configuration

The project uses the [Secrets Gradle Plugin for Android](https://github.com/google/secrets-gradle-plugin) to manage sensitive keys securely.

Ensure your app's `build.gradle` has the following configuration to safely access the API key:

```kotlin
android {
    defaultConfig {
        buildConfigField("String", "API_KEY", "\"${project.findProperty("apiKey")}\"")
    }
}
```

You can access the API key in code as follows:

```kotlin
val apiKey = BuildConfig.API_KEY
```

### 4. Build and Run the Project

1. Open the project in Android Studio.
2. Sync Gradle to ensure all dependencies are correctly set up.
3. Build and run the project on an Android device with SDK version 32 or higher.

## Usage

1. Launch the app.
2. Enter a prompt in the input field.
3. Press the "Go" button to send the prompt to the Gemini API.
4. View the generated response in the results section.

## Troubleshooting

- **404 Error for Gemini Model**: If you encounter a 404 error, ensure you’re using `gemini-1.5-flash` as the model name, as older models may be deprecated.
- **API Key Issues**: Verify that the API key is correctly placed in `local.properties` and accessible through `BuildConfig.API_KEY`.

## Dependencies

- **Kotlin**: Language used for the Android app.
- **Generative AI SDK**: Access the Gemini API models.
- **Secrets Gradle Plugin**: Manage sensitive data securely.

## License

This project is licensed under the MIT License. See the [LICENSE](https://www.notion.so/jones-wang/LICENSE) file for more details.