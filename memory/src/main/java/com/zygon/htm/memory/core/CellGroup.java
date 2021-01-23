package com.zygon.htm.memory.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.zygon.htm.core.Identifier;
import com.zygon.htm.core.message.Destination;
import com.zygon.htm.core.message.Message;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class CellGroup {

    private static final class Watcher extends Thread {

        private final Collection<Cell> cells;

        public Watcher(Collection<Cell> cells) {
            super("Watcher");
            super.setDaemon(true);

            this.cells = cells;
        }

        @Override
        public void run() {
            while (true) {

                try {
                    Thread.sleep(2000);
                } catch (Throwable ignore) {
                }

                StringBuilder sb = new StringBuilder();

                this.cells.forEach((cell) -> {
                    sb.append(cell.getPrinter().print()).append("\n");
                });

                System.out.println(sb);
            }
        }
    }

    // 1D dimensional for now
    private static Collection<Destination> getNeighbors(int idx, Cell[] cells, int radius) {
        Collection<Destination> neighbors = Lists.newArrayList();

        int min = Math.max(idx - (radius / 2), 0);
        int max = Math.min(idx + (radius / 2), cells.length);

        for (int i = min; i < max; i++) {
            if (i != idx) {
                neighbors.add(cells[i]);
            }
        }

        return neighbors;
    }

    // Testing out using all cells as neighbors - this means direct routing
    // and really no residual activations
    private static Collection<Destination> getAllOthers(int idx, Cell[] cells) {
        Collection<Destination> neighbors = Lists.newArrayList();

        for (int i = 0; i < cells.length; i++) {
            if (i != idx) {
                neighbors.add(cells[i]);
            }
        }

        return neighbors;
    }

    private static final AbstractScheduledService.Scheduler CELL_SCHEDULER
            = AbstractScheduledService.Scheduler.newFixedDelaySchedule(0, 500, TimeUnit.MILLISECONDS);

    private final Identifier groupId;
    private final Map<Identifier, Cell> cellsById = Maps.newHashMap();

    // For monitoring active cells
    private final Set<Identifier> activeCellIds = Sets.newHashSet();

    // Consider an Identifer generator for different cell configurations
    // Also, maybe just plain old cell configuration properties like radius
    public CellGroup(Identifier groupId, int cellCount) {
        Preconditions.checkArgument(groupId != null);
        Preconditions.checkArgument(cellCount > 0);

        this.groupId = groupId;

        Cell[] cells = new Cell[cellCount];

        // Create cells - just 1D for now
        for (int i = 0; i < cellCount; i++) {
            cells[i] = new Cell(this, new Identifier(i), CELL_SCHEDULER);
        }

        // Attach neighbors
        for (int i = 0; i < cellCount; i++) {
//            Collection<Cell> neighbors = getNeighbors(i, cells, 4);
            Collection<Destination> neighbors = getAllOthers(i, cells);
            cells[i].setNeighbors(neighbors);
        }

        // put into map
        for (int i = 0; i < cellCount; i++) {
            this.cellsById.put(cells[i].getIdentifier(), cells[i]);
        }
    }

    public void doStart() {
        // Start cells
        for (Cell cell : this.cellsById.values()) {
            cell.startAsync();
        }

        // Start simple watcher
        new Watcher(this.cellsById.values()).start();
    }

    public void doStop() {
        // Stop cells
        for (Cell cell : this.cellsById.values()) {
            cell.stopAsync();
        }
    }

    public Identifier[] getActiveCells() {
        return this.activeCellIds.toArray(new Identifier[this.activeCellIds.size()]);
    }

    public int getCellCount() {
        return this.cellsById.size();
    }

    /*pkg*/ void notifyActive(Identifier id) {
        Preconditions.checkArgument(this.cellsById.containsKey(id));
        Preconditions.checkState(!this.activeCellIds.contains(id));

        this.activeCellIds.add(id);
    }

    public void reset() {
        this.activeCellIds.clear();

        for (Cell cell : this.cellsById.values()) {
            cell.reset();
        }
    }

    public void send(Message message) {
        // Send the message to ALL cells - cells have to manage their own
        // universe in relation to other cells.
        for (Cell cell : this.cellsById.values()) {
            cell.getQueue().put(message);
        }
    }

    public void send(Message... messages) {
        for (Message msg : messages) {
            this.send(msg);
        }
    }
}
