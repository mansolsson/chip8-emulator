#ifndef GRAPHICS_H
#define GRAPHICS_H

#include <stdbool.h>

#define SCALE 10
#define SCREEN_WIDTH 64
#define SCREEN_HEIGHT 48

void create_window();
void clear_window();
void refresh_window();
void draw_rectangle(int x, int y, int width, int height);
void close_window();
void update_window(bool *screen);

#endif