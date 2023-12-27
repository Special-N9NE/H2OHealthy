package org.n9ne.auth.databinding;
import org.n9ne.auth.R;
import org.n9ne.auth.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentCompleteProfileBindingImpl extends FragmentCompleteProfileBinding implements org.n9ne.auth.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.iv, 2);
        sViewsWithIds.put(R.id.tvTitle, 3);
        sViewsWithIds.put(R.id.tvDescription, 4);
        sViewsWithIds.put(R.id.tilActivity, 5);
        sViewsWithIds.put(R.id.spActivity, 6);
        sViewsWithIds.put(R.id.tilGender, 7);
        sViewsWithIds.put(R.id.spGender, 8);
        sViewsWithIds.put(R.id.etWeight, 9);
        sViewsWithIds.put(R.id.etHeight, 10);
        sViewsWithIds.put(R.id.tvKg, 11);
        sViewsWithIds.put(R.id.tvCm, 12);
        sViewsWithIds.put(R.id.bNext, 13);
        sViewsWithIds.put(R.id.guideline, 14);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback5;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentCompleteProfileBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 15, sIncludes, sViewsWithIds));
    }
    private FragmentCompleteProfileBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (androidx.appcompat.widget.AppCompatButton) bindings[13]
            , (androidx.appcompat.widget.AppCompatEditText) bindings[1]
            , (androidx.appcompat.widget.AppCompatEditText) bindings[10]
            , (androidx.appcompat.widget.AppCompatEditText) bindings[9]
            , (androidx.constraintlayout.widget.Guideline) bindings[14]
            , (android.widget.ImageView) bindings[2]
            , (android.widget.AutoCompleteTextView) bindings[6]
            , (android.widget.AutoCompleteTextView) bindings[8]
            , (com.google.android.material.textfield.TextInputLayout) bindings[5]
            , (com.google.android.material.textfield.TextInputLayout) bindings[7]
            , (android.widget.TextView) bindings[12]
            , (android.widget.TextView) bindings[4]
            , (android.widget.TextView) bindings[11]
            , (android.widget.TextView) bindings[3]
            );
        this.etBirthday.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        mCallback5 = new org.n9ne.auth.generated.callback.OnClickListener(this, 1);
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
            setViewModel((org.n9ne.auth.viewModel.CompleteProfileViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setViewModel(@Nullable org.n9ne.auth.viewModel.CompleteProfileViewModel ViewModel) {
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
        org.n9ne.auth.viewModel.CompleteProfileViewModel viewModel = mViewModel;
        // batch finished
        if ((dirtyFlags & 0x2L) != 0) {
            // api target 1

            this.etBirthday.setOnClickListener(mCallback5);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        // localize variables for thread safety
        // viewModel
        org.n9ne.auth.viewModel.CompleteProfileViewModel viewModel = mViewModel;
        // viewModel != null
        boolean viewModelJavaLangObjectNull = false;



        viewModelJavaLangObjectNull = (viewModel) != (null);
        if (viewModelJavaLangObjectNull) {


            viewModel.dateClick();
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