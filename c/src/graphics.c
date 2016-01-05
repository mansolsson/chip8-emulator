#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <SDL2/SDL.h>
#include "graphics.h"
#include "chip8.h"

static SDL_Window *window;

void create_window()
{
    SDL_Surface *screenSurface = NULL; 
    if(SDL_Init(SDL_INIT_VIDEO) < 0) { 
        fprintf(stderr, "Failed to initialize SDL: %s\n", SDL_GetError());
        exit(1);
    } else {
    	window = SDL_CreateWindow("Chip-8 Emulator", SDL_WINDOWPOS_UNDEFINED, SDL_WINDOWPOS_UNDEFINED, 
    		SCREEN_WIDTH * SCALE, SCREEN_HEIGHT * SCALE, SDL_WINDOW_SHOWN); 
    	if(window == NULL) { 
    		fprintf(stderr, "Failed to create window: %s\n", SDL_GetError());
    		exit(1);
    	}
    }
}

void clear_window() 
{
	SDL_Surface * screenSurface = SDL_GetWindowSurface(window); 
	SDL_FillRect(screenSurface, NULL, SDL_MapRGB(screenSurface->format, 0xFF, 0xFF, 0xFF)); 
}

void refresh_window()
{
	SDL_Surface * screenSurface = SDL_GetWindowSurface(window); 
	SDL_UpdateWindowSurface(window);
}

void draw_rectangle(int x, int y, int width, int height)
{
	SDL_Surface * screenSurface = SDL_GetWindowSurface(window);
	SDL_Rect srcrect;
	srcrect.x = x;
	srcrect.y = y;
	srcrect.w = width;
	srcrect.h = height;

	SDL_FillRect(screenSurface, &srcrect, SDL_MapRGB(screenSurface->format, 0x00, 0x00, 0x00)); 
}

void close_window()
{
	SDL_DestroyWindow(window);
    SDL_Quit();
}

void update_window(bool *screen) 
{
	for(int i = 0; i < SCREEN_SIZE; i++) {
		if(screen[i]) {
			int x = (i % SCREEN_WIDTH) * SCALE;
			int y = (i / SCREEN_WIDTH) * SCALE;
			draw_rectangle(x, y, SCALE, SCALE);
		}
	}
}
