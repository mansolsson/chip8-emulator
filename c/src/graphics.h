#pragma once

#include <stdbool.h>

#define SCALE 10
#define SCREEN_WIDTH 64
#define SCREEN_HEIGHT 48

void create_window();
void clear_window();
void refresh_window();
void draw_rectangle(int32_t x, int32_t y, int32_t width, int32_t height);
void close_window();
void update_window(bool *screen);
