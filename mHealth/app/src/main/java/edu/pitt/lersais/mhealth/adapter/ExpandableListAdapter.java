package edu.pitt.lersais.mhealth.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import edu.pitt.lersais.mhealth.R;
import edu.pitt.lersais.mhealth.model.ExpandedMenu;

/**
 * The expandable list adapter for ExpandableListView.
 *
 * @author Haobing Huang and Runhua Xu.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private List<ExpandedMenu> mMenuList;
    private HashMap<ExpandedMenu, List<ExpandedMenu>> mMenuListChild;
    private ExpandableListView expandableListView;
    private Context context;

    public ExpandableListAdapter(Context context,
                                 List<ExpandedMenu> menuList,
                                 HashMap<ExpandedMenu, List<ExpandedMenu>> mMenuListChild,
                                 ExpandableListView view) {
        super();
        this.context = context;
        this.mMenuList = menuList;
        this.mMenuListChild = mMenuListChild;
        this.expandableListView = view;
    }

    @Override
    public int getGroupCount() {
        return mMenuList.size();
    }

    @Override
    public int getChildrenCount(int group) {
        if (mMenuListChild.containsKey(mMenuList.get(group))) {
            return mMenuListChild.get(mMenuList.get(group)).size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int group) {

        return mMenuList.get(group);
    }

    @Override
    public Object getChild(int group, int child) {

        return mMenuListChild.get(mMenuList.get(group)).get(child);
    }

    @Override
    public long getGroupId(int group) {

        return group;
    }

    @Override
    public long getChildId(int group, int child) {

        return child;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int group, boolean b, View view, ViewGroup viewGroup) {
        ExpandedMenu expandedMenu = (ExpandedMenu) getGroup(group);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.nav_menu_expandable_item, null);
        }

        TextView menuItemTextView = view.findViewById(R.id.expandable_menu_item_name);
        menuItemTextView.setText(expandedMenu.getMenuName());
        menuItemTextView.setTypeface(null, Typeface.BOLD);

        ImageView menuItemImageView = view.findViewById(R.id.expandable_menu_item_icon);
        menuItemImageView.setImageResource(expandedMenu.getMenuIconId());

        return view;
    }

    @Override
    public View getChildView(int group, int child, boolean b, View view, ViewGroup viewGroup) {
        ExpandedMenu expandedMenu = (ExpandedMenu) getChild(group, child);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.nav_menu_expandable_subitem, null);
        }
        TextView menuItemTextView = view.findViewById(R.id.expandable_menu_item_name);
        menuItemTextView.setText(expandedMenu.getMenuName());
        ImageView menuItemImageView = view.findViewById(R.id.expandable_menu_item_icon);
        menuItemImageView.setImageResource(expandedMenu.getMenuIconId());
        return view;
    }

    @Override
    public boolean isChildSelectable(int group, int child) {
        return true;
    }
}
