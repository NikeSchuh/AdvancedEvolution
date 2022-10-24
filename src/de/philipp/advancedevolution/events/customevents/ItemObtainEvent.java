package de.philipp.advancedevolution.events.customevents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ItemObtainEvent extends Event {

    public HandlerList handlerList = new HandlerList();



    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
