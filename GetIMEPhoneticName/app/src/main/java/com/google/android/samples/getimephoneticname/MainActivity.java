/*
 * Copyright (C) 2021 -  Google LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.samples.getimephoneticname;

/* FILE: MainActivity.java
 * PURPOSE: This small app demonstrates how to retrieve phonetic information from a Text Field
 *          receiving Japanese input with IME, via a TextWatcher.
 *
 * DETAILS:
 *          Note in the accompanying resources definition that the field must meet these conditions:
 *           android:inputType="textPersonName"
 *           android:privateImeOptions="com.google.android.inputmethod.latin.requestPhoneticOutput"
 *          In this sample code, fields that don't meet this condition are also included as
 *          counter-examples.
 *          To see this in effect follolw this procedure:
 *              - Use IME with the input language set to Japanese
 *              - type a Japanese name (complete or incomplete)
 *              - choose one of the candidates
 *          Expected result:
 *              - the field with R.id.lastPhoneticInfoDisplay gets briefly highlighted in blue,
 *                and shows extracted phonetic information
 *
 */

import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.Editable;
import android.text.style.TtsSpan;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity {
    private EventHighlighter eventHighlighter;
    private EventHighlighter eventHighlighterCumulative;

    private class PhoneticRetriever implements TextWatcher {
        PhoneticRetriever( EditText target, EditText cumulative ) {
            this._target = target;
            this._cumulativeTarget = cumulative;
        }

        // Extracts phonetic metadata from an incoming text blob
        private String getPhoneticMetadata(CharSequence s) {
            String phonetic = null;
            if (s instanceof SpannableStringBuilder) {
                SpannableStringBuilder textAsSpan = (SpannableStringBuilder) s;
                TtsSpan[] allSpans = textAsSpan.getSpans(0, s.length(), TtsSpan.class);
                if (allSpans.length == 1 && allSpans[0].getType().equals(TtsSpan.TYPE_TEXT)) {
                    // log shows where the span is in the text
                    Log.v("PHON",
                            s.toString() + " [" + textAsSpan.length() + "] start:" +
                            textAsSpan.getSpanStart(allSpans[0]) + " end:" +
                            textAsSpan.getSpanEnd(allSpans[0]));
                    phonetic =  allSpans[0].getArgs().getString(TtsSpan.ARG_TEXT);
                    textAsSpan.removeSpan(allSpans[0]); // avoid consuming again
                }
            }
            return phonetic;
        }

        @Override
        public void afterTextChanged(Editable s) {
            String sphonetic = getPhoneticMetadata(s);
            if (sphonetic == null) {
                _target.setText( "No Phonetic");
                eventHighlighter.restartRed();
                eventHighlighterCumulative.restartRed();
            } else {
                _target.setText("Phonetic: " + sphonetic);
                _cumulativeTarget.setText(_cumulativeTarget.getText() + sphonetic);
                eventHighlighter.restartBlue();
                eventHighlighterCumulative.restartBlue();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        EditText _target;
        EditText _cumulativeTarget;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText etNoPhonetic = (EditText)findViewById(R.id.editTextNoPhonetic);
        EditText etPhoneticNoOptions = (EditText)findViewById(R.id.editTextPhoneticNoOptions );
        EditText etPhoneticWithOptions = (EditText)findViewById(R.id.editTextPhoneticWithOptions );

        EditText etResult =  (EditText)findViewById(R.id.lastPhoneticInfoDisplay );
        this.eventHighlighter = new EventHighlighter( etResult );

        EditText etCumulative =  (EditText)findViewById(R.id.cumulativeInfo );
        this.eventHighlighterCumulative = new EventHighlighter( etCumulative );

        etNoPhonetic.addTextChangedListener(new PhoneticRetriever(etResult, etCumulative));
        etPhoneticNoOptions.addTextChangedListener(new PhoneticRetriever(etResult, etCumulative));
        etPhoneticWithOptions.addTextChangedListener(new PhoneticRetriever(etResult, etCumulative));
    }
}
