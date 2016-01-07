#ifndef EMULATOR_H
#define EMULATOR_H

#include "chip8.h"
#include <SDL2/SDL.h>

void start_program(char *program_path);
long get_current_time_ms();
void handle_input(struct chip8 *c, bool *game_running);
void update_state(struct chip8 *c, SDL_Event key, bool value);

#endif