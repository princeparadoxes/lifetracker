package com.princeparadoxes.watertracker.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;
import java.util.Set;

/**
 * See: https://github.com/jansenfelipe/androidmask
 */
public class MaskEditTextChangedListener implements TextWatcher {

    private static MaskEditTextChangedListener instance;

    private String mMask;
    private EditText mEditText;
    private Set<String> symbolMask = new HashSet<String>();
    private boolean isUpdating;
    private String old = "";

    public MaskEditTextChangedListener(String mask, EditText editText) {
        mMask = mask;
        mEditText = editText;
        initSymbolMask();
    }

    private void initSymbolMask() {
        for (int i = 0; i < mMask.length(); i++) {
            char ch = mMask.charAt(i);
            if (ch != '#')
                symbolMask.add(String.valueOf(ch));
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String str = unmask(s.toString(), symbolMask);
        String mascara = "";

        if (isUpdating) {
            old = str;
            isUpdating = false;
            return;
        }

        if (str.length() > old.length())
            mascara = mask(mMask, str);
        else
            mascara = s.toString();

        isUpdating = true;

        mEditText.setText(mascara);
        mEditText.setSelection(mascara.length());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // nothing
    }


    @Override
    public void afterTextChanged(Editable s) {
        // nothing
    }

    public static String unmask(String s, Set<String> replaceSymbols) {

        for (String symbol : replaceSymbols)
            s = s.replaceAll("[" + symbol + "]", "");

        return s;
    }

    public static String mask(String format, String text) {
        String maskedText = "";
        int i = 0;
        for (char m : format.toCharArray()) {
            if (m != '#') {
                maskedText += m;
                continue;
            }
            try {
                maskedText += text.charAt(i);
            } catch (Exception e) {
                break;
            }
            i++;
        }
        return maskedText;
    }
}