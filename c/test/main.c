#include <check.h>
#include <stdlib.h>
#include "chip8_test.h"
#include "cpu_test.h"

int run_opcode_test();
int run_chip8_test();

int main(int argc, char **argv) 
{
	int number_failed = 0;
	number_failed += run_opcode_test();
	number_failed += run_chip8_test();
    return (number_failed == 0) ? EXIT_SUCCESS : EXIT_FAILURE;
}

int run_opcode_test() 
{
    Suite *s = opcode_suite();
    SRunner *sr = srunner_create(s);
    srunner_run_all(sr, CK_NORMAL);
    int number_failed = srunner_ntests_failed(sr);
    srunner_free(sr);
    return number_failed;
}

int run_chip8_test() 
{
    Suite *s = chip8_suite();
    SRunner *sr = srunner_create(s);
    srunner_run_all(sr, CK_NORMAL);
    int number_failed = srunner_ntests_failed(sr);
    srunner_free(sr);
    return number_failed;   
}