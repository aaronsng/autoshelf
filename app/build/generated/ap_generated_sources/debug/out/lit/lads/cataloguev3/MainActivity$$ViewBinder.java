// Generated code from Butter Knife. Do not modify!
package lit.lads.cataloguev3;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends lit.lads.cataloguev3.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230820, "field 'imageview'");
    target.imageview = finder.castView(view, 2131230820, "field 'imageview'");
    view = finder.findRequiredView(source, 2131230758, "field 'button'");
    target.button = finder.castView(view, 2131230758, "field 'button'");
    view = finder.findRequiredView(source, 2131230917, "field 'textView'");
    target.textView = finder.castView(view, 2131230917, "field 'textView'");
    view = finder.findRequiredView(source, 2131230766, "field 'characteristics'");
    target.characteristics = finder.castView(view, 2131230766, "field 'characteristics'");
    view = finder.findRequiredView(source, 2131230775, "field 'coloursX'");
    target.coloursX = finder.castView(view, 2131230775, "field 'coloursX'");
    view = finder.findRequiredView(source, 2131230761, "field 'catalogue'");
    target.catalogue = finder.castView(view, 2131230761, "field 'catalogue'");
  }

  @Override public void unbind(T target) {
    target.imageview = null;
    target.button = null;
    target.textView = null;
    target.characteristics = null;
    target.coloursX = null;
    target.catalogue = null;
  }
}
