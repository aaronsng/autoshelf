// Generated code from Butter Knife. Do not modify!
package lit.lads.cataloguev3;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Catalogue$$ViewBinder<T extends lit.lads.cataloguev3.Catalogue> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230774, "field 'colourEdit'");
    target.colourEdit = finder.castView(view, 2131230774, "field 'colourEdit'");
    view = finder.findRequiredView(source, 2131230785, "field 'descEdit'");
    target.descEdit = finder.castView(view, 2131230785, "field 'descEdit'");
    view = finder.findRequiredView(source, 2131230873, "field 'prompter'");
    target.prompter = finder.castView(view, 2131230873, "field 'prompter'");
    view = finder.findRequiredView(source, 2131230794, "field 'imageView'");
    target.imageView = finder.castView(view, 2131230794, "field 'imageView'");
  }

  @Override public void unbind(T target) {
    target.colourEdit = null;
    target.descEdit = null;
    target.prompter = null;
    target.imageView = null;
  }
}
