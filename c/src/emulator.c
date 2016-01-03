#include <time.h>
#include <math.h>
#include <SDL2/SDL.h>
#include "emulator.h"
#include "chip8.h"
#include "graphics.h"

#define OPCODES_PER_SECOND 500
#define MS_PER_INSTRUCTION (1000 / OPCODES_PER_SECOND)

void start_program(char *program_path)
{
	bool game_running = true;
	struct chip8 c;
	init_chip8(&c);
	load_program(&c, program_path);

	create_window();
	SDL_Event e;

	while(game_running) {
		long before = get_current_time_ms();
		while( SDL_PollEvent( &e ) != 0 ) {
			if( e.type == SDL_QUIT ) {
				game_running = false;
			} else if(e.type == SDL_KEYDOWN) {
				if(e.key.keysym.sym >= SDLK_KP_1 && e.key.keysym.sym <= SDLK_KP_9) {
					c.keys[e.key.keysym.sym - SDLK_KP_1 + 1] = true;
				} else if(e.key.keysym.sym == SDLK_KP_A) {
					c.keys[0] = true;
				}
			} else if(e.type == SDL_KEYUP) {
				if(e.key.keysym.sym >= SDLK_KP_1 && e.key.keysym.sym <= SDLK_KP_9) {
					c.keys[e.key.keysym.sym - SDLK_KP_1 + 1] = false;
				} else if(e.key.keysym.sym == SDLK_KP_A) {
					c.keys[0] = false;
				}
			}
		}

		uint16_t opcode = c.memory[c.pc];
		opcode <<= 8;
		opcode += c.memory[c.pc + 1];
		execute_opcode(&c, opcode);
		if(c.delay_timer > 0) {
			c.delay_timer--;	
		}
		if(c.sound_timer > 0) {
			c.sound_timer--;
		}
		clear_window();
		update_window(c.screen);
		refresh_window();
		long time_to_sleep = MS_PER_INSTRUCTION - (get_current_time_ms() - before);
		if(time_to_sleep > 0) {
			SDL_Delay(time_to_sleep);
		}
	}
	close_window();
}

long get_current_time_ms()
{
    struct timespec spec;
    clock_gettime(CLOCK_REALTIME, &spec);
    return spec.tv_sec * 1000 + round(spec.tv_nsec / 1.0e6);
}