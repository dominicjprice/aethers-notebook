package horizon.aether.ui;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * In order to add a button in the preferences activity, it was necessary 
 * to create a custom preference that wraps a normal button.
 */
public class ButtonPreference 
extends Preference {

    private static Button button;
    private static LinearLayout buttonParent;
    
    /**
     * Constructor.
     * @param context
     */
    public ButtonPreference(Context context) {
        super(context);
        button = new Button(getContext());
    }

    /**
     * Constructor.
     * @param context
     * @param attrs
     */
    public ButtonPreference(Context context, AttributeSet attrs) {  
        super(context, attrs);
        button = new Button(getContext());
    }
    
    /**
     * Constructor.
     * @param context
     * @param attrs
     * @param defStyle
     */
    public ButtonPreference(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle); 
        button = new Button(getContext());
    }
    
    /**
     * Creates the View to be shown for this Preference in the PreferenceActivity.
     */
    @Override
    protected View onCreateView(ViewGroup parent) {
        LinearLayout layout = new LinearLayout(getContext());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.gravity = Gravity.LEFT;
        params.weight  = 1.0f;
        button.setText(getTitle());
        button.setLayoutParams(params);
       
        if (button.getParent() != null) {
            buttonParent.removeView(button);
        }
        buttonParent = layout;
        layout.addView(button);
        
        layout.setId(android.R.id.widget_frame);

        return layout; 
    }
  
    /**
     * Sets the on click listener for the button.
     * @param listener
     */
    public void setOnClickListener(OnClickListener listener) {
        button.setOnClickListener(listener);
    }

    /**
     * Sets the button's text.
     * @param text
     */
    public void setText(String text) {
        button.setText(text);
    }
    
}
