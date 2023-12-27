package org.n9ne.auth.databinding;
import org.n9ne.auth.R;
import org.n9ne.auth.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentLoginBindingImpl extends FragmentLoginBinding implements org.n9ne.auth.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.textView, 5);
        sViewsWithIds.put(R.id.textView2, 6);
        sViewsWithIds.put(R.id.etEmail, 7);
        sViewsWithIds.put(R.id.etPassword, 8);
        sViewsWithIds.put(R.id.bLogin, 9);
        sViewsWithIds.put(R.id.gTop, 10);
        sViewsWithIds.put(R.id.linearLayout2, 11);
        sViewsWithIds.put(R.id.gBottom, 12);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback3;
    @Nullable
    private final android.view.View.OnClickListener mCallback4;
    @Nullable
    private final android.view.View.OnClickListener mCallback1;
    @Nullable
    private final android.view.View.OnClickListener mCallback2;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentLoginBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 13, sIncludes, sViewsWithIds));
    }
    private FragmentLoginBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (androidx.appcompat.widget.AppCompatButton) bindings[9]
            , (androidx.appcompat.widget.AppCompatEditText) bindings[7]
            , (androidx.appcompat.widget.AppCompatEditText) bindings[8]
            , (androidx.constraintlayout.widget.Guideline) bindings[12]
            , (androidx.constraintlayout.widget.Guideline) bindings[10]
            , (android.widget.ImageView) bindings[4]
            , (android.widget.ImageView) bindings[1]
            , (android.widget.LinearLayout) bindings[11]
            , (android.widget.LinearLayout) bindings[3]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[6]
            , (android.widget.TextView) bindings[2]
            );
        this.ivGoogle.setTag(null);
        this.ivPassword.setTag(null);
        this.llRegister.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.tvForgot.setTag(null);
        setRootTag(root);
        // listeners
        mCallback3 = new org.n9ne.auth.generated.callback.OnClickListener(this, 3);
        mCallback4 = new org.n9ne.auth.generated.callback.OnClickListener(this, 4);
        mCallback1 = new org.n9ne.auth.generated.callback.OnClickListener(this, 1);
        mCallback2 = new org.n9ne.auth.generated.callback.OnClickListener(this, 2);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.viewModel == variableId) {
            setViewModel((org.n9ne.auth.viewModel.LoginViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setViewModel(@Nullable org.n9ne.auth.viewModel.LoginViewModel ViewModel) {
        this.mViewModel = ViewModel;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.viewModel);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        org.n9ne.auth.viewModel.LoginViewModel viewModel = mViewModel;
        // batch finished
        if ((dirtyFlags & 0x2L) != 0) {
            // api target 1

            this.ivGoogle.setOnClickListener(mCallback4);
            this.ivPassword.setOnClickListener(mCallback1);
            this.llRegister.setOnClickListener(mCallback3);
            this.tvForgot.setOnClickListener(mCallback2);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        switch(sourceId) {
            case 3: {
                // localize variables for thread safety
                // viewModel
                org.n9ne.auth.viewModel.LoginViewModel viewModel = mViewModel;
                // viewModel != null
                boolean viewModelJavaLangObjectNull = false;



                viewModelJavaLangObjectNull = (viewModel) != (null);
                if (viewModelJavaLangObjectNull) {


                    viewModel.registerClick();
                }
                break;
            }
            case 4: {
                // localize variables for thread safety
                // viewModel
                org.n9ne.auth.viewModel.LoginViewModel viewModel = mViewModel;
                // viewModel != null
                boolean viewModelJavaLangObjectNull = false;



                viewModelJavaLangObjectNull = (viewModel) != (null);
                if (viewModelJavaLangObjectNull) {


                    viewModel.googleClick();
                }
                break;
            }
            case 1: {
                // localize variables for thread safety
                // viewModel
                org.n9ne.auth.viewModel.LoginViewModel viewModel = mViewModel;
                // viewModel != null
                boolean viewModelJavaLangObjectNull = false;



                viewModelJavaLangObjectNull = (viewModel) != (null);
                if (viewModelJavaLangObjectNull) {


                    viewModel.passwordClick();
                }
                break;
            }
            case 2: {
                // localize variables for thread safety
                // viewModel
                org.n9ne.auth.viewModel.LoginViewModel viewModel = mViewModel;
                // viewModel != null
                boolean viewModelJavaLangObjectNull = false;



                viewModelJavaLangObjectNull = (viewModel) != (null);
                if (viewModelJavaLangObjectNull) {


                    viewModel.forgetPasswordClick();
                }
                break;
            }
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): viewModel
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}