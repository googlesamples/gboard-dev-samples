# GetIMEPhoneticOut

This project is a sample application intended to demonstrate capturing Japanese
language phonetic information from an IME in Android.
Detecting phonetic output is especially helpful when inputting person names
in Japanese, as the mapping from pictographic (Kanji) characters into 
phonetic characters is complex and not easily converted to rules.
The sample application has a name (MIME type) edit box which declares a special private IME option.
Doing so causes incoming text to be attached to spans (TtsSpans to be precise),
which contain phonetic information about the text chosen by the user.
This interface is first implemented by Gboard (Beta as of Jan 2021).
