package org.n9ne.auth;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.n9ne.auth.databinding.FragmentCompleteProfileBindingImpl;
import org.n9ne.auth.databinding.FragmentLoginBindingImpl;
import org.n9ne.auth.databinding.FragmentLoginDoneBindingImpl;
import org.n9ne.auth.databinding.FragmentRegisterBindingImpl;
import org.n9ne.auth.databinding.FragmentSplashBindingImpl;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_FRAGMENTCOMPLETEPROFILE = 1;

  private static final int LAYOUT_FRAGMENTLOGIN = 2;

  private static final int LAYOUT_FRAGMENTLOGINDONE = 3;

  private static final int LAYOUT_FRAGMENTREGISTER = 4;

  private static final int LAYOUT_FRAGMENTSPLASH = 5;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(5);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(org.n9ne.auth.R.layout.fragment_complete_profile, LAYOUT_FRAGMENTCOMPLETEPROFILE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(org.n9ne.auth.R.layout.fragment_login, LAYOUT_FRAGMENTLOGIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(org.n9ne.auth.R.layout.fragment_login_done, LAYOUT_FRAGMENTLOGINDONE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(org.n9ne.auth.R.layout.fragment_register, LAYOUT_FRAGMENTREGISTER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(org.n9ne.auth.R.layout.fragment_splash, LAYOUT_FRAGMENTSPLASH);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_FRAGMENTCOMPLETEPROFILE: {
          if ("layout/fragment_complete_profile_0".equals(tag)) {
            return new FragmentCompleteProfileBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_complete_profile is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTLOGIN: {
          if ("layout/fragment_login_0".equals(tag)) {
            return new FragmentLoginBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_login is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTLOGINDONE: {
          if ("layout/fragment_login_done_0".equals(tag)) {
            return new FragmentLoginDoneBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_login_done is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTREGISTER: {
          if ("layout/fragment_register_0".equals(tag)) {
            return new FragmentRegisterBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_register is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTSPLASH: {
          if ("layout/fragment_splash_0".equals(tag)) {
            return new FragmentSplashBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_splash is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(2);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    result.add(new org.n9ne.common.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(3);

    static {
      sKeys.put(0, "_all");
      sKeys.put(1, "item");
      sKeys.put(2, "viewModel");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(5);

    static {
      sKeys.put("layout/fragment_complete_profile_0", org.n9ne.auth.R.layout.fragment_complete_profile);
      sKeys.put("layout/fragment_login_0", org.n9ne.auth.R.layout.fragment_login);
      sKeys.put("layout/fragment_login_done_0", org.n9ne.auth.R.layout.fragment_login_done);
      sKeys.put("layout/fragment_register_0", org.n9ne.auth.R.layout.fragment_register);
      sKeys.put("layout/fragment_splash_0", org.n9ne.auth.R.layout.fragment_splash);
    }
  }
}
