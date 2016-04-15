/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage;


import com.eowise.recyclerview.stickyheaders.samples.data.ConcreteData2;
import com.eowise.recyclerview.stickyheaders.samples.data.NotificationData;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;

import java.util.LinkedList;
import java.util.List;

public class ExampleDataProvider extends AbstractDataProvider {
    private List<ConcreteData2> mData;
    private ConcreteData2 mLastRemovedData;
    private int mLastRemovedPosition = -1;

    public ExampleDataProvider() {

        mData = new LinkedList<>();
/*

        for (int j = 0; j < atoz.length(); j++) {
            final long id = mData.size();
            final int viewType = 0;
            final String text = Character.toString(atoz.charAt(j));
            final int swipeReaction = RecyclerViewSwipeManager.REACTION_CAN_SWIPE_UP | RecyclerViewSwipeManager.REACTION_CAN_SWIPE_DOWN;
            if (j == 0)
                mData.add(new ConcreteData2(id, viewType, text, swipeReaction, NotificationType.FOLLOWING));

            else if (j == 1)
                mData.add(new ConcreteData2(id, viewType, text, swipeReaction, NotificationType.SWAPREQUEST));
            else if (j == 2)
                mData.add(new ConcreteData2(id, viewType, text, swipeReaction, NotificationType.CHECKOUT));

            else if (j == 3)
                mData.add(new ConcreteData2(id, viewType, text, swipeReaction, NotificationType.DECNIED));
            else if (j == 4)
                mData.add(new ConcreteData2(id, viewType, text, swipeReaction, NotificationType.USERMENTION));
            else if (j == 5)
                mData.add(new ConcreteData2(id, viewType, text, swipeReaction, NotificationType.PURCHASEDITEM));
            else if (j == 6)
                mData.add(new ConcreteData2(id, viewType, text, swipeReaction, NotificationType.POSTSHARED));
            else if (j == 7)
                mData.add(new ConcreteData2(id, viewType, text, swipeReaction, NotificationType.BLANK));


        }*/

    }

    @Override
    public int getCount() {
        return mData.size();
    }


    @Override
    public Data getItem(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException("index = " + index);
        }

        return mData.get(index);
    }

    @Override
    public int undoLastRemoval() {
        if (mLastRemovedData != null) {
            int insertedPosition;
            if (mLastRemovedPosition >= 0 && mLastRemovedPosition < mData.size()) {
                insertedPosition = mLastRemovedPosition;
            } else {
                insertedPosition = mData.size();
            }

            mData.add(insertedPosition, mLastRemovedData);

            mLastRemovedData = null;
            mLastRemovedPosition = -1;

            return insertedPosition;
        } else {
            return -1;
        }
    }

    @Override
    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }

        final ConcreteData2 item = mData.remove(fromPosition);

        mData.add(toPosition, item);
        mLastRemovedPosition = -1;
    }

    @Override
    public void addItem(NotificationData nd) {
        final long id = mData.size();
        final int viewType = 0;
        final int swipeReaction = RecyclerViewSwipeManager.REACTION_CAN_SWIPE_UP | RecyclerViewSwipeManager.REACTION_CAN_SWIPE_DOWN;
        switch (nd.getNotitype()) {
            case "1":
                mData.add(new ConcreteData2(id, viewType, nd, swipeReaction, NotificationType.FOLLOWING));

                break;
            case "2":
                mData.add(new ConcreteData2(id, viewType, nd, swipeReaction, NotificationType.SWAPREQUEST));
                break;
            case "3":
                mData.add(new ConcreteData2(id, viewType,nd, swipeReaction, NotificationType.CHECKOUT));

                break;
            case "5":
                mData.add(new ConcreteData2(id, viewType, nd, swipeReaction, NotificationType.USERMENTION));
            case "7":
                mData.add(new ConcreteData2(id, viewType, nd, swipeReaction, NotificationType.POSTLIKED));

                break;
            case "8":
                mData.add(new ConcreteData2(id, viewType, nd, swipeReaction, NotificationType.BLANK));

                break;
            case "9":
                mData.add(new ConcreteData2(id, viewType, nd, swipeReaction, NotificationType.USERACCEPTEDCHECKOUT));

                break;

            case "10":
                mData.add(new ConcreteData2(id, viewType, nd, swipeReaction, NotificationType.POSTSHARED));

                break;
        }


    }

    @Override
    public void removeItem(int position) {
        //noinspection UnnecessaryLocalVariable
        final ConcreteData2 removedItem = mData.remove(position);

        mLastRemovedData = removedItem;
        mLastRemovedPosition = position;
    }


}
