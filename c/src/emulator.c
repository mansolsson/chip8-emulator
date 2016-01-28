#include <time.h>
#include <math.h>
#include <stdint.h>
#include <stdbool.h>
#include <SDL2/SDL.h>
#include "emulator.h"
#include "chip8.h"
#include "graphics.h"
#include "cpu.h"

#define OPCODES_PER_SECOND 600
#define TIMER_COUNTDOWN_PER_SECOND 60
#define MS_PER_COUNTDOWN (1000 / TIMER_COUNTDOWN_PER_SECOND)
#define MS_PER_INSTRUCTION (1000 / OPCODES_PER_SECOND)

void start_program(char *program_path)
{
	bool game_running = true;
	struct chip8 c;
	init_chip8(&c);
	load_program(&c, program_path);

	create_window();
	int64_t time_since_last_countdown = 0;

	while(game_running) {
		int64_t before = get_current_time_ms();
		handle_input(&c, &game_running);

		uint16_t opcode = c.memory[c.pc];
		opcode <<= 8;
		opcode += c.memory[c.pc + 1];
		execute_opcode(&c, opcode);
		clear_window();
		update_window(c.screen);
		refresh_window();

		int64_t elapsed_time = get_current_time_ms() - before;
		int64_t time_to_sleep = MS_PER_INSTRUCTION - elapsed_time;
		time_since_last_countdown += elapsed_time;
		if(time_since_last_countdown >= MS_PER_COUNTDOWN) {
			if(c.delay_timer > 0) {
				c.delay_timer--;	
			}
			if(c.sound_timer > 0) {
				c.sound_timer--;
			}	
			time_since_last_countdown = 0;
		}

		if(time_to_sleep > 0) {
			time_since_last_countdown += time_to_sleep;
			SDL_Delay(time_to_sleep);
		}
	}
	close_window();
}

int64_t get_current_time_ms()
{
    struct timespec spec;
    clock_gettime(CLOCK_REALTIME, &spec);
    return spec.tv_sec * 1000 + round(spec.tv_nsec / 1.0e6);
}

void handle_input(struct chip8 *c, bool *game_running)
{
	SDL_Event e;
	while(SDL_PollEvent(&e) != 0) {
		if(e.type == SDL_QUIT) {
			*game_running = false;
		} else if(e.type == SDL_KEYDOWN) {
			update_state(c, e, true);
		} else if(e.type == SDL_KEYUP) {
			update_state(c, e, false);
		}
	}
}

void update_state(struct chip8 *c, SDL_Event e, bool value)
{
	switch(e.key.keysym.sym) {
	case SDLK_KP_PERIOD:
		c->keys[0x0] = value;
		break;
	case SDLK_KP_7:
		c->keys[0x1] = value;
		break;
	case SDLK_KP_8:
		c->keys[0x2] = value;
		break;
	case SDLK_KP_9:
		c->keys[0x3] = value;
		break;
	case SDLK_KP_4:
		c->keys[0x4] = value;
		break;
	case SDLK_KP_5:
		c->keys[0x5] = value;
		break;
	case SDLK_KP_6:
		c->keys[0x6] = value;
		break;
	case SDLK_KP_1:
		c->keys[0x7] = value;
		break;
	case SDLK_KP_2:
		c->keys[0x8] = value;
		break;
	case SDLK_KP_3:
		c->keys[0x9] = value;
		break;
	case SDLK_KP_0:
		c->keys[0xA] = value;
		break;
	case SDLK_KP_ENTER:
		c->keys[0xB] = value;
		break;
	case SDLK_KP_DIVIDE:
		c->keys[0xC] = value;
		break;
	case SDLK_KP_MULTIPLY:
		c->keys[0xD] = value;
		break;
	case SDLK_KP_MINUS:
		c->keys[0xE] = value;
		break;
	case SDLK_KP_PLUS:
		c->keys[0xF] = value;
		break;
	}
}
