/*
 * Copyright (C) 2016 skydoves
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.waterdays.ui.adapters;

/** Developed by skydoves on 2017-08-20. Copyright (c) 2017 skydoves rights reserved. */
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.skydoves.waterdays.ui.viewholders.BaseViewHolder;
import com.trello.rxlifecycle2.android.ActivityEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {
  private List<List<Object>> sections = new ArrayList<>();

  public List<List<Object>> sections() {
    return sections;
  }

  public void clearSections() {
    sections.clear();
  }

  public <T> void addSection(final @NonNull List<T> section) {
    sections.add(new ArrayList<>(section));
  }

  public <T> void addSections(final @NonNull List<List<T>> sections) {
    for (final List<T> section : sections) {
      addSection(section);
    }
  }

  public <T> void setSection(final int location, final @NonNull List<T> section) {
    sections.set(location, new ArrayList<>(section));
  }

  public <T> void insertSection(final int location, final @NonNull List<T> section) {
    sections.add(location, new ArrayList<>(section));
  }

  public <T> void removeSection(final int location) {
    sections.remove(location);
  }

  public <T> void sectionOrderChange(final int location) {
    Collections.reverse(sections.get(location));
  }

  /** Fetch the layout id associated with a sectionRow. */
  protected abstract int layout(final @NonNull SectionRow sectionRow);

  /** Returns a new KSViewHolder given a layout and view. */
  protected abstract @NonNull BaseViewHolder viewHolder(
      final @LayoutRes int layout, final @NonNull View view);

  @Override
  public void onViewDetachedFromWindow(final @NonNull BaseViewHolder holder) {
    super.onViewDetachedFromWindow(holder);

    // View holders are "stopped" when they are detached from the window for recycling
    holder.lifecycleEvent(ActivityEvent.STOP);

    // View holders are "destroy" when they are detached from the window and no adapter is listening
    // to events, so ostensibly the view holder is being deallocated.
    if (!hasObservers()) {
      holder.lifecycleEvent(ActivityEvent.DESTROY);
    }
  }

  @Override
  public void onViewAttachedToWindow(final @NonNull BaseViewHolder holder) {
    super.onViewAttachedToWindow(holder);

    // View holders are "started" when they are attached to the new window because this means
    // it has been recycled.
    holder.lifecycleEvent(ActivityEvent.START);
  }

  @Override
  public final @NonNull BaseViewHolder onCreateViewHolder(
      final @NonNull ViewGroup viewGroup, final @LayoutRes int layout) {
    final View view = inflateView(viewGroup, layout);
    final BaseViewHolder viewHolder = viewHolder(layout, view);

    viewHolder.lifecycleEvent(ActivityEvent.CREATE);

    return viewHolder;
  }

  @Override
  public final void onBindViewHolder(final @NonNull BaseViewHolder viewHolder, final int position) {
    final Object data = objectFromPosition(position);

    try {
      viewHolder.bindData(data);
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public final int getItemViewType(final int position) {
    return layout(sectionRowFromPosition(position));
  }

  @Override
  public final int getItemCount() {
    int itemCount = 0;
    for (final List<?> section : sections) {
      itemCount += section.size();
    }

    return itemCount;
  }

  /** Gets the data object associated with a sectionRow. */
  protected Object objectFromSectionRow(final @NonNull SectionRow sectionRow) {
    return sections.get(sectionRow.section()).get(sectionRow.row());
  }

  protected int sectionCount(final int section) {
    if (section > sections().size() - 1) {
      return 0;
    }
    return sections().get(section).size();
  }

  /** Gets the data object associated with a position. */
  protected Object objectFromPosition(final int position) {
    return objectFromSectionRow(sectionRowFromPosition(position));
  }

  private @NonNull SectionRow sectionRowFromPosition(final int position) {
    final SectionRow sectionRow = new SectionRow();
    int cursor = 0;
    for (final List<?> section : sections) {
      for (final Object __ : section) {
        if (cursor == position) {
          return sectionRow;
        }
        cursor++;
        sectionRow.nextRow();
      }
      sectionRow.nextSection();
    }

    throw new RuntimeException("Position " + position + " not found in sections");
  }

  private @NonNull View inflateView(
      final @NonNull ViewGroup viewGroup, final @LayoutRes int viewType) {
    final LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
    return layoutInflater.inflate(viewType, viewGroup, false);
  }

  /** SectionRows allow RecyclerViews to be structured into sections of rows. */
  protected class SectionRow {
    private int section;
    private int row;

    public SectionRow() {
      section = 0;
      row = 0;
    }

    public SectionRow(final int section, final int row) {
      this.section = section;
      this.row = row;
    }

    public int section() {
      return section;
    }

    public int row() {
      return row;
    }

    protected void nextRow() {
      row++;
    }

    protected void nextSection() {
      section++;
      row = 0;
    }
  }
}
