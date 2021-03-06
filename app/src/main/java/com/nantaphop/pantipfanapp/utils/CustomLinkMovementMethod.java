package com.nantaphop.pantipfanapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Layout;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.MotionEvent;

import com.nantaphop.pantipfanapp.BaseApplication;
import com.nantaphop.pantipfanapp.ForumActivity_;
import com.nantaphop.pantipfanapp.TopicActivity_;
import com.nantaphop.pantipfanapp.event.OpenPhotoEvent;
import com.nantaphop.pantipfanapp.model.ForumPagerItem;
import com.nantaphop.pantipfanapp.response.Topic;
import com.nantaphop.pantipfanapp.service.PantipRestClient;

/**
 * Created with IntelliJ IDEA.
 * User: Nantaphop
 * Date: 8/14/13
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomLinkMovementMethod extends LinkMovementMethod {

    private static Context movementContext;

    private static CustomLinkMovementMethod linkMovementMethod = new CustomLinkMovementMethod();

    public boolean onTouchEvent(android.widget.TextView widget, android.text.Spannable buffer, MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);
            if (link.length != 0) {
                String url = link[0].getURL();
                if (url.contains("http")) {

//                    ((BaseApplication)movementContext.getApplicationContext()).getTracker().sendEvent("user_action", "open_link_internal", url, null);


                    if (url.contains("pantip.com/topic")) {
                        final Intent i = new Intent(movementContext, TopicActivity_.class);
                        Topic topic = new Topic();
                        topic.setId(Integer.parseInt(url.split("/")[4]));
                        topic.setTitle(url);
                        i.putExtra("topic", topic);
                        movementContext.startActivity(i);
                        Log.d("fragment", "open topic");
                    } else if (url.contains("pantip.com/forum") || url.contains("pantip.com/tag")) {

                        Intent i = new Intent(movementContext, ForumActivity_.class);
                        ForumPagerItem forumPagerItem = new ForumPagerItem(url, Utils.getForumPath(url));
                        i.putExtra("forumPagerItem", forumPagerItem);
                        if (url.contains("pantip.com/forum"))
                            i.putExtra("forumType", PantipRestClient.ForumType.Room);
                        else if (url.contains("pantip.com/tag"))
                            i.putExtra("forumType", PantipRestClient.ForumType.Tag);
                        movementContext.startActivity(i);
                    } else if (url.startsWith("http://f.ptcdn.info/")) {
                        BaseApplication.getEventBus().post(new OpenPhotoEvent(url));
                    } else {
//
//                        Log.d("Link", url);
//                        Intent i = new Intent(movementContext, WebViewActivity_.class);
//                        i.putExtra(WebViewActivity.EXTRA_URL, url);
//                        movementContext.startActivity(i);

                        Uri uri = Uri.parse(url);
                        movementContext.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    }
                }

//                else if (url.contains("tel")){
//                    Log.d("Link", url);
//                    Toast.makeText(movementContext, "Tel was clicked", Toast.LENGTH_LONG).show();
//                }
//
//                else if(url.contains("mailto")) {
//                    Log.d("Link", url);
//                    Toast.makeText(movementContext, "Mail link was clicked", Toast.LENGTH_LONG).show();
//                }
                return true;
            }
        }

        return super.onTouchEvent(widget, buffer, event);
    }

    public static android.text.method.MovementMethod getInstance(Context c) {
        movementContext = c;
//        EasyTracker.getInstance().setContext(movementContext);
        return linkMovementMethod;
    }
}
