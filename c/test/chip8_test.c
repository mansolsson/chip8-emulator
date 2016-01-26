#include <check.h>
#include <stdlib.h>
#include <stdint.h>
#include "../src/chip8.h"

START_TEST (init_chip8_clears_screen) 
{
	struct chip8 c;

	init_chip8(&c);

	for(int i = 0; i < SCREEN_SIZE; i++) {
		ck_assert(c.screen[i] == false);	
	}
}
END_TEST

START_TEST (init_chip8_set_all_keys_not_pressed)
{
	struct chip8 c;

	init_chip8(&c);

	for(int i = 0; i < NR_KEYS; i++) {
		ck_assert(c.keys[i] == false);
	}
}
END_TEST

START_TEST (init_chip8_place_sprites_in_memory)
{
	struct chip8 c;

	init_chip8(&c);

	for(int i = 0; i < 5 * 16; i++) {
		ck_assert(c.memory[i] != 0);
	}
}
END_TEST

START_TEST (init_chip8_clear_memory) 
{
	struct chip8 c;

	init_chip8(&c);

	int after_sprites = 5 * 16;
	for(int i = after_sprites; i < MEMORY_SIZE; i++) {
		ck_assert(c.memory[i] == 0);
	}
}
END_TEST

START_TEST (init_chip8_pc_set_to_start_of_program)
{
	struct chip8 c;

	init_chip8(&c);

	ck_assert(c.pc == PROGRAM_START);
}
END_TEST

START_TEST (init_chip8_stack_index_set_to_zero) 
{
	struct chip8 c;

	init_chip8(&c);

	ck_assert(c.stack_index == 0);
}
END_TEST

START_TEST (init_chip8_sound_timer_set_to_zero) 
{
	struct chip8 c;

	init_chip8(&c);

	ck_assert(c.sound_timer == 0);
}
END_TEST

START_TEST (init_chip8_delay_timer_set_to_zero) 
{
	struct chip8 c;

	init_chip8(&c);

	ck_assert(c.delay_timer == 0);
}
END_TEST

Suite * opcode_suite()
{
    Suite *suite;
    TCase *tc_core;

    suite = suite_create("Opcode");
    tc_core = tcase_create("Core");

    tcase_add_test(tc_core, init_chip8_clears_screen);
    tcase_add_test(tc_core, init_chip8_clear_memory);
    tcase_add_test(tc_core, init_chip8_place_sprites_in_memory);
    tcase_add_test(tc_core, init_chip8_set_all_keys_not_pressed);
    tcase_add_test(tc_core, init_chip8_stack_index_set_to_zero);
    tcase_add_test(tc_core, init_chip8_pc_set_to_start_of_program);
    tcase_add_test(tc_core, init_chip8_delay_timer_set_to_zero);
    tcase_add_test(tc_core, init_chip8_sound_timer_set_to_zero);

    suite_add_tcase(suite, tc_core);

    return suite;
}

int main()
{
    int number_failed;
    Suite *s;
    SRunner *sr;

    s = opcode_suite();
    sr = srunner_create(s);

    srunner_run_all(sr, CK_NORMAL);
    number_failed = srunner_ntests_failed(sr);
    srunner_free(sr);
    return (number_failed == 0) ? EXIT_SUCCESS : EXIT_FAILURE;
}
