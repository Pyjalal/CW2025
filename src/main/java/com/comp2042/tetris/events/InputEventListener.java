package com.comp2042.tetris.events;


import com.comp2042.tetris.models.DownData;

import com.comp2042.tetris.models.ViewData;

import com.comp2042.tetris.events.MoveEvent;
public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    void createNewGame();
}
