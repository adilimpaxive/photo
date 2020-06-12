package com.photo.viedo.maker.photomaker;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TextEditorDialogFragment extends DialogFragment {

    public static final String TAG = TextEditorDialogFragment.class.getSimpleName();
    public static final String EXTRA_INPUT_TEXT = "extra_input_text";
    public static final String EXTRA_COLOR_CODE = "extra_color_code";
    private EditText mAddTextEditText;
    private TextView mAddTextDoneTextView;
    private InputMethodManager mInputMethodManager;
    private int mColorCode;
    private TextEditor mTextEditor;
    TextView tt1,tt2,tt3,tt4,tt5,tt6,tt7,tt8,tt9,tt10,tt11,tt12,tt13,tt14,tt15;
    private String var;
    Typeface f233tf;
    String Input;
    HorizontalScrollView f231hs;;
    public interface TextEditor {
        void onDone(String inputText, int colorCode, String font);
    }


    //Show dialog with provide text and text color
    public static TextEditorDialogFragment show(@NonNull AppCompatActivity appCompatActivity,
                                                @NonNull String inputText,
                                                @ColorInt int colorCode) {
        Bundle args = new Bundle();
        args.putString(EXTRA_INPUT_TEXT, inputText);
        args.putInt(EXTRA_COLOR_CODE, colorCode);
        TextEditorDialogFragment fragment = new TextEditorDialogFragment();
        fragment.setArguments(args);
        fragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return fragment;
    }

    //Show dialog with default text input as empty and text color white
    public static TextEditorDialogFragment show(@NonNull AppCompatActivity appCompatActivity) {
        return show(appCompatActivity,
                "", ContextCompat.getColor(appCompatActivity, R.color.white));
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        //Make dialog full screen with transparent background
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.add_text_dialog, container, false);
        this.f231hs = (HorizontalScrollView) v. findViewById(R.id.hs);
        tt1 = v.findViewById(R.id.tt1);
        tt1.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "f2.ttf"));
        this.tt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextEditorDialogFragment.this.var = "f2.ttf";
                TextEditorDialogFragment.this.f233tf = Typeface.createFromAsset(TextEditorDialogFragment.this.getActivity().getAssets(), "f2.ttf");
                TextEditorDialogFragment.this.mAddTextEditText.setTypeface(TextEditorDialogFragment.this.f233tf);
            }
        });
        tt2 = v.findViewById(R.id.tt2);
        tt2.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font1.ttf"));
        this.tt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextEditorDialogFragment.this.var = "font1.ttf";
                TextEditorDialogFragment.this.f233tf = Typeface.createFromAsset(TextEditorDialogFragment.this.getActivity().getAssets(), "font1.ttf");
                TextEditorDialogFragment.this.mAddTextEditText.setTypeface(TextEditorDialogFragment.this.f233tf);
            }
        });
        tt3 = v.findViewById(R.id.tt3);
        tt3.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font2.ttf"));
        this.tt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextEditorDialogFragment.this.var = "font2.ttf";
                TextEditorDialogFragment.this.f233tf = Typeface.createFromAsset(TextEditorDialogFragment.this.getActivity().getAssets(), "font2.ttf");
                TextEditorDialogFragment.this.mAddTextEditText.setTypeface(TextEditorDialogFragment.this.f233tf);
            }
        });
        tt4 = v.findViewById(R.id.tt4);
        tt4.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font31.ttf"));
        this.tt4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextEditorDialogFragment.this.var = "font31.ttf";
                TextEditorDialogFragment.this.f233tf = Typeface.createFromAsset(TextEditorDialogFragment.this.getActivity().getAssets(), "font31.ttf");
                TextEditorDialogFragment.this.mAddTextEditText.setTypeface(TextEditorDialogFragment.this.f233tf);
            }
        });
        tt5 = v.findViewById(R.id.tt5);
        tt5.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font5.otf"));
        this.tt5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextEditorDialogFragment.this.var = "font5.otf";
                TextEditorDialogFragment.this.f233tf = Typeface.createFromAsset(TextEditorDialogFragment.this.getActivity().getAssets(), "font5.otf");
                TextEditorDialogFragment.this.mAddTextEditText.setTypeface(TextEditorDialogFragment.this.f233tf);
            }
        });
        tt6 = v.findViewById(R.id.tt6);
        tt6.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"font32.otf"));
        this.tt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextEditorDialogFragment.this.var = "font32.otf";
                TextEditorDialogFragment.this.f233tf = Typeface.createFromAsset(TextEditorDialogFragment.this.getActivity().getAssets(),"font32.otf");
                TextEditorDialogFragment.this.mAddTextEditText.setTypeface(TextEditorDialogFragment.this.f233tf);
            }
        });
        tt7 = v.findViewById(R.id.tt7);
        tt7.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"font11.otf"));
        this.tt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextEditorDialogFragment.this.var = "font11.otf";
                TextEditorDialogFragment.this.f233tf = Typeface.createFromAsset(TextEditorDialogFragment.this.getActivity().getAssets(),"font11.otf");
                TextEditorDialogFragment.this.mAddTextEditText.setTypeface(TextEditorDialogFragment.this.f233tf);
            }
        });
        tt8 = v.findViewById(R.id.tt8);
        tt8.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"font15.ttf"));
        this.tt8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextEditorDialogFragment.this.var = "font15.ttf";
                TextEditorDialogFragment.this.f233tf = Typeface.createFromAsset(TextEditorDialogFragment.this.getActivity().getAssets(),"font15.ttf");
                TextEditorDialogFragment.this.mAddTextEditText.setTypeface(TextEditorDialogFragment.this.f233tf);
            }
        });

        tt9 = v.findViewById(R.id.tt9);
        tt9.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"font17.ttf"));
        this.tt9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextEditorDialogFragment.this.var = "font17.ttf";
                TextEditorDialogFragment.this.f233tf = Typeface.createFromAsset(TextEditorDialogFragment.this.getActivity().getAssets(),"font17.ttf");
                TextEditorDialogFragment.this.mAddTextEditText.setTypeface(TextEditorDialogFragment.this.f233tf);
            }
        });
        tt10 = v.findViewById(R.id.tt10);
        tt10.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"font21.ttf"));
        this.tt10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextEditorDialogFragment.this.var = "font21.ttf";
                TextEditorDialogFragment.this.f233tf = Typeface.createFromAsset(TextEditorDialogFragment.this.getActivity().getAssets(),"font21.ttf");
                TextEditorDialogFragment.this.mAddTextEditText.setTypeface(TextEditorDialogFragment.this.f233tf);
            }
        });
        tt11 = v.findViewById(R.id.tt11);
        tt11.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"font8.otf"));
        this.tt11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextEditorDialogFragment.this.var = "font8.otf";
                TextEditorDialogFragment.this.f233tf = Typeface.createFromAsset(TextEditorDialogFragment.this.getActivity().getAssets(),"font8.otf");
                TextEditorDialogFragment.this.mAddTextEditText.setTypeface(TextEditorDialogFragment.this.f233tf);
            }
        });
        tt12 = v.findViewById(R.id.tt12);
        tt12.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"font27.otf"));
        this.tt12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextEditorDialogFragment.this.var = "font27.otf";
                TextEditorDialogFragment.this.f233tf = Typeface.createFromAsset(TextEditorDialogFragment.this.getActivity().getAssets(),"font27.otf");
                TextEditorDialogFragment.this.mAddTextEditText.setTypeface(TextEditorDialogFragment.this.f233tf);
            }
        });
        tt13 = v.findViewById(R.id.tt13);
        tt13.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"font28.otf"));
        this.tt13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextEditorDialogFragment.this.var = "font28.otf";
                TextEditorDialogFragment.this.f233tf = Typeface.createFromAsset(TextEditorDialogFragment.this.getActivity().getAssets(),"font28.otf");
                TextEditorDialogFragment.this.mAddTextEditText.setTypeface(TextEditorDialogFragment.this.f233tf);
            }
        });
        tt14 = v.findViewById(R.id.tt14);
        tt14.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"font30.otf"));
        this.tt14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextEditorDialogFragment.this.var = "font30.otf";
                TextEditorDialogFragment.this.f233tf = Typeface.createFromAsset(TextEditorDialogFragment.this.getActivity().getAssets(),"font30.otf");
                TextEditorDialogFragment.this.mAddTextEditText.setTypeface(TextEditorDialogFragment.this.f233tf);
            }
        });
        tt15 = v.findViewById(R.id.tt15);
        tt15.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"font33.ttf"));
        this.tt15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextEditorDialogFragment.this.var = "font33.ttf";
                TextEditorDialogFragment.this.f233tf = Typeface.createFromAsset(TextEditorDialogFragment.this.getActivity().getAssets(),"font33.ttf");
                TextEditorDialogFragment.this.mAddTextEditText.setTypeface(TextEditorDialogFragment.this.f233tf);
            }
        });
        return v;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAddTextEditText = view.findViewById(R.id.add_text_edit_text);
        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mAddTextDoneTextView = view.findViewById(R.id.add_text_done_tv);

        //Setup the color picker for text color
        RecyclerView addTextColorPickerRecyclerView = view.findViewById(R.id.add_text_color_picker_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        addTextColorPickerRecyclerView.setLayoutManager(layoutManager);
        addTextColorPickerRecyclerView.setHasFixedSize(true);
        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(getActivity());
        //This listener will change the text color when clicked on any color from picker
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                mColorCode = colorCode;
                mAddTextEditText.setTextColor(colorCode);
            }
        });
        addTextColorPickerRecyclerView.setAdapter(colorPickerAdapter);
        mAddTextEditText.setText(getArguments().getString(EXTRA_INPUT_TEXT));
        mColorCode = getArguments().getInt(EXTRA_COLOR_CODE);
        mAddTextEditText.setTextColor(mColorCode);
        mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        //Make a callback on activity when user is done with text editing
        mAddTextDoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                dismiss();
                String inputText = mAddTextEditText.getText().toString();
                if (!TextUtils.isEmpty(inputText) && mTextEditor != null) {
                    mTextEditor.onDone(inputText, mColorCode, var);
                }
            }
        });

    }


    //Callback to listener if user is done with text editing
    public void setOnTextEditorListener(TextEditor textEditor) {
        mTextEditor = textEditor;
    }
}

