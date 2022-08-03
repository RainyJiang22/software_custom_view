package com.rainy.popupmenu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import androidx.annotation.MenuRes;
import androidx.appcompat.widget.PopupMenu;
import com.rainy.popupmenu.databinding.ContentMenuItemBinding;
import com.rainy.popupmenu.databinding.ContentPopupMenuBinding;

/**
 * @author jacky
 * @date 2021/10/20
 */
public class ContentPopupMenu {

    public interface IMenuItemClickListener {
        void menuItemClicked(int id);
    }

    private View anchorView;
    private PopupWindow popupWindow;

    private final Context context;
    private final LayoutInflater inflater;
    private final ContentPopupMenuBinding binding;

    private int xOffset, yOffset, gravity;
    private boolean shouldDrawGroupSeparator = true;
    private boolean isFavorite = false;

    private IMenuItemClickListener clickListener;

    @SuppressLint("NewApi")
    private ContentPopupMenu(Context context) {
        this.context = context;
        this.inflater = context.getSystemService(LayoutInflater.class);
        this.binding = ContentPopupMenuBinding.inflate(inflater);
    }

    private ContentPopupMenu setAnchorAndParams(View anchor, int xOffset, int yOffset, int gravity) {
        this.anchorView = anchor;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.gravity = gravity;
        return this;
    }

    private ContentPopupMenu setSeparationEnabled(boolean enabled) {
        this.shouldDrawGroupSeparator = enabled;
        return this;
    }

    private ContentPopupMenu setClickListener(IMenuItemClickListener listener) {
        this.clickListener = listener;
        return this;
    }

    private ContentPopupMenu setMenuResources(Integer... menuResources) {
        for (Integer resource : menuResources) {

            PopupMenu temporary = new PopupMenu(context, null);
            temporary.inflate(resource);
            addItemsToWindow(temporary.getMenu());
        }
        return this;
    }

    public ContentPopupMenu setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
        return this;
    }

    private ContentPopupMenu addItemsToWindow(Menu menu) {

//        if (BuildConfig.LOG_ENABLE) {
//            Log.d("PopupMenu", "addItemsToWindow: " + menu.size());
//        }
        for (int i = 0; i < menu.size(); i++) {

            final MenuItem menuItem = menu.getItem(i);

            final ContentMenuItemBinding menuItemBinding = ContentMenuItemBinding.inflate(inflater, binding.getRoot(), false);

            String itemTitle = menuItem.getTitle().toString();

            menuItemBinding.tvMenuText.setText(itemTitle);
            menuItemBinding.tvMenuText.setId(menuItem.getItemId());

            if (menuItem.getIcon() != null) {
                menuItemBinding.ivMenuType.setImageDrawable(menuItem.getIcon());
            } else {
                menuItemBinding.ivMenuType.setVisibility(View.GONE);
            }


            menuItemBinding.getRoot().setOnClickListener(v -> {
                if (menuItem.hasSubMenu()) {
                    new MenuBuilder(context)
                            .setAnchorAndParams(anchorView, xOffset, yOffset, gravity)
                            .setSeparationEnabled(shouldDrawGroupSeparator)
                            .setClickListener(clickListener)
                            .setFavorite(isFavorite)
                            .setMenuResources().build()
                            .addItemsToWindow(menuItem.getSubMenu())
                            .showMenu();
                } else clickListener.menuItemClicked(menuItem.getItemId());
                popupWindow.dismiss();
            });

            binding.getRoot().addView(menuItemBinding.getRoot());
        }
        return this;
    }

    private int dpToPx(Context context) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 228, r.getDisplayMetrics());
    }

    public void showMenu() {
        popupWindow = new PopupWindow(binding.getRoot(), dpToPx(context), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        backgroundAlpha(0.6f);
        int[] windowPos = calculatePopMenuPos(context, anchorView, popupWindow.getContentView());
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, windowPos[0], windowPos[1]);
        popupWindow.setOnDismissListener(() -> backgroundAlpha(1.0f));
    }

    public static class MenuBuilder {

        private final Context context;

        private View anchorView;
        private Integer[] menuResources;
        private IMenuItemClickListener clickListener;

        private int xOffset, yOffset, gravity;
        private boolean shouldDrawGroupSeparator = true;
        private boolean isFavorite = false;

        public MenuBuilder(Context context) {
            this.context = context;
        }

        public MenuBuilder setAnchorAndParams(View anchor, int xOffset, int yOffset, int gravity) {
            this.anchorView = anchor;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.gravity = gravity;
            return this;
        }

        public MenuBuilder setSeparationEnabled(boolean enabled) {
            this.shouldDrawGroupSeparator = enabled;
            return this;
        }

        public MenuBuilder setFavorite(boolean isFavorite) {
            this.isFavorite = isFavorite;
            return this;
        }

        public MenuBuilder setClickListener(IMenuItemClickListener listener) {
            this.clickListener = listener;
            return this;
        }

        public MenuBuilder setMenuResources(@MenuRes Integer... menuResources) {
            this.menuResources = menuResources;
            return this;
        }

        public ContentPopupMenu build() {

            return new ContentPopupMenu(context)
                    .setAnchorAndParams(anchorView, xOffset, yOffset, gravity)
                    .setSeparationEnabled(shouldDrawGroupSeparator)
                    .setFavorite(isFavorite)
                    .setClickListener(clickListener)
                    .setMenuResources(menuResources);
        }
    }


    //半透明遮罩
    private void backgroundAlpha(float alpha) {
        WindowManager.LayoutParams layoutParams = ((Activity) context).getWindow().getAttributes();
        layoutParams.alpha = alpha;
        ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ((Activity) context).getWindow().setAttributes(layoutParams);
    }


    /***
     *
     * 计算出来的位置
     * 如果描点的view位置有变化，可以自己适当加入额外的偏移来修正
     * @param anchorView 呼出window的view
     * @param contentView  当前window的内容布局
     * @return window左上角显示的xOff, yOff坐标
     */
    private static int[] calculatePopMenuPos(Context context, View anchorView, View contentView) {
        int[] windowPos = new int[2];
        int[] anchorLoc = new int[2];

        //获取当前描点左上角的坐标
        anchorView.getLocationOnScreen(anchorLoc);
        int anchorHeight = anchorView.getMeasuredHeight();
        int anchorWidth = anchorView.getMeasuredWidth();
        int anchorPaddingTop = anchorView.getPaddingTop();

        int screenHeight = ScreenUtil.getScreenHeight(context);
        int screenWidth = ScreenUtil.getScreenWidth(context);

        //计算contentView
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        int windowHeight = contentView.getMeasuredHeight();
        int windowWidth = contentView.getMeasuredWidth();

        boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = anchorLoc[0] - (windowWidth + anchorWidth) / 2;
            windowPos[1] = anchorLoc[1] - windowHeight - ScreenUtil.dip2px(context, 4f);
        } else {
            windowPos[0] = anchorLoc[0] - (windowWidth + anchorWidth) / 2;
            windowPos[1] = anchorLoc[1] + anchorHeight - anchorPaddingTop / 2;
        }
        return windowPos;
    }

}
