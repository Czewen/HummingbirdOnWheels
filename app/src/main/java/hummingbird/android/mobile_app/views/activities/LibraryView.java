package hummingbird.android.mobile_app.views.activities;

import hummingbird.android.mobile_app.views.fragments.LibraryFragmentAdapter;
import hummingbird.android.mobile_app.views.fragments.LibraryListFragment;

/**
 * Created by CzeWen on 2016-01-21.
 */
public interface LibraryView {

    public LibraryListFragment getCurrentFragment();

    public LibraryListFragment getFragment(String name);
}
