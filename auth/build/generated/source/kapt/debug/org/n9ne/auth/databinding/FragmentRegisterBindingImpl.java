package org.n9ne.auth.databinding;
import org.n9ne.auth.R;
import org.n9ne.auth.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentRegisterBindingImpl extends FragmentRegisterBinding implements org.n9ne.auth.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.textView, 4);
        sViewsWithIds.put(R.id.etName, 5);
        sViewsWithIds.put(R.id.etEmail, 6);
        sViewsWithIds.put(R.id.etPassword, 7);
        sViewsWithIds.put(R.id.bRegister, 8);
        sViewsWithIds.put(R.id.cbPolicy, 9);
        sViewsWithIds.put(R.id.gTop, 10);
        sViewsWithIds.put(R.id.linearLayout2, 11);
        sViewsWithIds.put(R.id.gBottom, 12);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback7;
    @Nullable
    private final android.view.View.OnClickListener mCallback8;
    @Nullable
    private final android.view.View.OnClickListener mCallback6;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentRegisterBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 13, sIncludes, sViewsWithIds));
    }
    private FragmentRegisterBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (androidx.appcompat.widget.AppCompatButton) bindings[8]
            , (android.widget.CheckBox) bindings[9]
            , (androidx.appcompat.widget.AppCompatEditText) bindings[6]
            , (androidx.appcompat.widget.AppCompatEditText) bindings[5]
            , (androidx.appcompat.widget.AppCompatEditText) bindings[7]
            , (androidx.constraintlayout.widget.Guideline) bindings[12]
            , (androidx.constraintlayout.widget.Guideline) bindings[10]
            , (android.widget.ImageView) bindings[3]
            , (android.widget.ImageView) bindings[1]
            , (android.widget.LinearLayout) bindings[11]
            , (android.widget.LinearLayout) bindings[2]
            , (android.widget.TextView) bindings[4]
            );
        this.ivGoogle.setTag(null);
        this.ivPassword.setTag(null);
        this.llLogin.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        mCallback7 = new org.n9ne.auth.generated.callback.OnClickListener(this, 2);
        mCallback8 = new org.n9ne.auth.generated.callback.OnClickListener(this, 3);
        mCallback6 = new org.n9ne.auth.generated.callback.OnClickListener(this, 1);
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
            setViewModel((org.n9ne.auth.viewModel.RegisterViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setViewModel(@Nullable org.n9ne.auth.viewModel.RegisterViewModel ViewModel) {
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
        org.n9ne.auth.viewModel.RegisterViewModel viewModel = mViewModel;
        // batch finished
        if ((dirtyFlags & 0x2L) != 0) {
            // api target 1

            this.ivGoogle.setOnClickListener(mCallback8);
            this.ivPassword.setOnClickListener(mCallback6);
            this.llLogin.setOnClickListener(mCallback7);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        switch(sourceId) {
            case 2: {
                // localize variables for thread safety
                // viewModel
                org.n9ne.auth.viewModel.RegisterViewModel viewModel = mViewModel;
                // viewModel != null
                boolean viewModelJavaLangObjectNull = false;



                viewModelJavaLangObjectNull = (viewModel) != (null);
                if (viewModelJavaLangObjectNull) {


                    viewModel.loginClick();
                }
                break;
            }
            case 3: {
                // localize variables for thread safety
                // viewModel
                org.n9ne.auth.viewModel.RegisterViewModel viewModel = mViewModel;
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
                org.n9ne.auth.viewModel.RegisterViewModel viewModel = mViewModel;
                // viewModel != null
                boolean viewModelJavaLangObjectNull = false;



                viewModelJavaLangObjectNull = (viewModel) != (null);
                if (viewModelJavaLangObjectNull) {


                    viewModel.passwordClick();
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