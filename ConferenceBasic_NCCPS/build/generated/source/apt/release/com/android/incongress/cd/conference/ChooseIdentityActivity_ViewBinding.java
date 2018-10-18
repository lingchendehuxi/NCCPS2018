// Generated code from Butter Knife. Do not modify!
package com.android.incongress.cd.conference;

import android.view.View;
import android.widget.RadioGroup;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class ChooseIdentityActivity_ViewBinding<T extends ChooseIdentityActivity> implements Unbinder {
  protected T target;

  private View view2131624122;

  private View view2131624123;

  private View view2131624124;

  public ChooseIdentityActivity_ViewBinding(final T target, Finder finder, Object source) {
    this.target = target;

    View view;
    target.mRradioGroup = finder.findRequiredViewAsType(source, R.id.rg_group, "field 'mRradioGroup'", RadioGroup.class);
    view = finder.findRequiredView(source, R.id.rb_paticipant, "method 'onParticipant'");
    view2131624122 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onParticipant();
      }
    });
    view = finder.findRequiredView(source, R.id.rb_unregister_one, "method 'OnUnRegisterOne'");
    view2131624123 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.OnUnRegisterOne();
      }
    });
    view = finder.findRequiredView(source, R.id.rb_unregister_two, "method 'OnUnRegisterTwo'");
    view2131624124 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.OnUnRegisterTwo();
      }
    });
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mRradioGroup = null;

    view2131624122.setOnClickListener(null);
    view2131624122 = null;
    view2131624123.setOnClickListener(null);
    view2131624123 = null;
    view2131624124.setOnClickListener(null);
    view2131624124 = null;

    this.target = null;
  }
}
