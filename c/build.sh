#!/bin/sh
set -e;

prepare() {
	if [ -d "build" ]; then
		rm -r build;
	fi
	mkdir -p build/test;
}

run_tests() {
	gcc src/chip8.c src/cpu.c test/chip8_test.c test/cpu_test.c test/main.c \
		-std=gnu11 -lcheck -lm -lpthread -lrt \
		-o build/test/chip8;
	build/test/chip8;
}

compile() {
	gcc src/chip8.c src/graphics.c src/cpu.c src/emulator.c src/main.c \
		-std=gnu11 -Wall -Wextra -lSDL2 -lm -lrt \
		-o build/chip8;
}

main() {
	prepare;
	run_tests;
	compile;
}
main;
