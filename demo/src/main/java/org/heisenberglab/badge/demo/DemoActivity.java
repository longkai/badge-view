package org.heisenberglab.badge.demo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.heisenberglab.badge.BadgeView;

/**
 * Created by longkai on 10/1/15.
 */
public class DemoActivity extends FragmentActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.demo_layout);

    FrameLayout container = (FrameLayout) findViewById(R.id.container);

    // NOTE: the child view must use the same layout-params with the badge!
    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    );

    BadgeView badgeView = new BadgeView(this);
    badgeView.setLayoutParams(lp);

    TextView tv = new TextView(this);
    tv.setLayoutParams(lp);
    tv.setText("This\n  is  \n  a  \n  tall  \n   text:-)");

    badgeView.addBadgeView(tv);

    badgeView.showBadge("...");
    badgeView.setBadgeLocation(BadgeView.BadgeLocation.BOTTOM_LEFT);

    // add to the container
    container.addView(badgeView);
  }
}
