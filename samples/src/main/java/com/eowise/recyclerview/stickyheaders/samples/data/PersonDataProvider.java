package com.eowise.recyclerview.stickyheaders.samples.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aurel on 11/10/14.
 */
public class PersonDataProvider {

    private HashMap<String, Boolean> items;
    private List<String> addedItems;

    public PersonDataProvider(List<String> persons)
    {
       this.items = new HashMap<String, Boolean>();
        this.addedItems = new ArrayList<String>();
        Log.e("size before addded", items.size()+","+persons.size());

        for (int i = 0; i < persons.size(); i++ )
        {
            items.put(i+persons.get(i), true);
            Log.e("size after addded", items.size() + "");
        }

        buildAddedItems();
    }


    public List<String> getItems() {

        Log.e("size", addedItems.size() + "");
        return addedItems;
    }

    public void remove(int position) {
        items.put(addedItems.get(position), false);
        buildAddedItems();
    }

    public int insertAfter(int position) {
        String addAfter = addedItems.get(position);
        Iterator<String> iterator = items.keySet().iterator();
        String next = iterator.next();

        while (iterator.hasNext() && !next.equals(addAfter)) {
            next = iterator.next();
        }

        do {
            next = iterator.next();
        }
        while (iterator.hasNext() && items.get(next));

        items.put(next, true);
        buildAddedItems();

        return addedItems.lastIndexOf(next);
    }

    private void buildAddedItems() {
        addedItems.clear();
        for (Map.Entry<String, Boolean> entry : items.entrySet()) {
            if (entry.getValue()) {
                addedItems.add(entry.getKey());
                Log.e("additems",entry.getKey());
            }
        }
        //Collections.sort(addedItems);
    }

   // private static List<String> persons = new ArrayList<String>();

    public void update(int position, String name) {
        addedItems.set(position, name);
    }
}
