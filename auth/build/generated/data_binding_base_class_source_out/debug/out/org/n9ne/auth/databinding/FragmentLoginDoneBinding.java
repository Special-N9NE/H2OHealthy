// Generated by data binding compiler. Do not edit!
package org.n9ne.auth.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.Guideline;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import java.lang.Deprecated;
import java.lang.Object;
import org.n9ne.auth.R;

public abstract class FragmentLoginDoneBinding extends ViewDataBinding {
  @NonNull
  public final AppCompatButton bGo;

  @NonNull
  public final Guideline guideline;

  @NonNull
  public final Guideline guideline2;

  @NonNull
  public final ImageView iv;

  @NonNull
  public final LinearLayout llTitle;

  @NonNull
  public final TextView tvDescription;

  @NonNull
  public final TextView tvName;

  protected FragmentLoginDoneBinding(Object _bindingComponent, View _root, int _localFieldCount,
      AppCompatButton bGo, Guideline guideline, Guideline guideline2, ImageView iv,
      LinearLayout llTitle, TextView tvDescription, TextView tvName) {
    super(_bindingComponent, _root, _localFieldCount);
    this.bGo = bGo;
    this.guideline = guideline;
    this.guideline2 = guideline2;
    this.iv = iv;
    this.llTitle = llTitle;
    this.tvDescription = tvDescription;
    this.tvName = tvName;
  }

  @NonNull
  public static FragmentLoginDoneBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_login_done, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static FragmentLoginDoneBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<FragmentLoginDoneBinding>inflateInternal(inflater, R.layout.fragment_login_done, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentLoginDoneBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_login_done, null, false, component)
   */
  @NonNull
  @Deprecated
  public static FragmentLoginDoneBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<FragmentLoginDoneBinding>inflateInternal(inflater, R.layout.fragment_login_done, null, false, component);
  }

  public static FragmentLoginDoneBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static FragmentLoginDoneBinding bind(@NonNull View view, @Nullable Object component) {
    return (FragmentLoginDoneBinding)bind(component, view, R.layout.fragment_login_done);
  }
}
