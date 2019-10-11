#include<stdio.h>
#include<stdlib.h>

typedef struct Setting {
	int nplayers;
	int height;
	int width;
	char *tokens;
} Settings;

typedef struct Games {
	int *board;
	Settings *settings;
} Game;

Settings *newSettings(int nplayers, int width, int height, char *tokens) {
	Settings *settings = malloc(sizeof(Settings));
	if (settings != NULL) {
		settings->tokens = tokens;
		settings->height = height;
		settings->width = width;
		settings->tokens = tokens;
	}
	return settings;
};

Game *newGame(Settings *settings) {
	Game *game;
	game = (Game *)malloc(sizeof(Game));
	if (game != NULL) {
		game->settings = settings;
		game->board = calloc(settings->height * settings->width, sizeof(int));
	}
	return game;
}

int inputValidated(int min, int max) {
	int option;
	option = max + 1;
	while (option < min || option > max) {
		fprintf(stdout, "Option (%d-%d): ", min, max);
		fscanf(stdin, "%d", &option);
	}
	return option;
}

int menuMain() {
	int option;
	fprintf(stdout,"Select an entry:\n\t1) Start new hotseat game\n\t2) Start new AI game\n\t3) Configure game settings\n\t4) List highscores\n\t5) Exit\n");
	option = inputValidated(1, 5);
	return option;
}

int getPlayerIndex(Game *game, int x, int y) {
	return game->board[x + y * game->settings->height];
}

void draw(Game *game) {
	char *buffer = calloc(game->settings->width * game->settings->height + game->settings->height + 1 + game->settings->width * 2 + game->settings->height * 2 + 4 + 2, sizeof(int));
	char *current = buffer;
	*(current++) = '+';
	for (int x = 0; x < game->settings->width; x++) {
		*(current++) = '-';
	} 
	*(current++) = '+';
	*(current++) = '\n';
	for (int y = 0; y < game->settings->height; y++) {
		*(current++) = '|';
		for (int x = 0; x < game->settings->width; x++) {
			*(current++) = game->settings->tokens[getPlayerIndex(game, x, y)];
		}
		*(current++) = '|';
		*(current++) = '\n';
	}
	*(current++) = '+';
	for (int x = 0; x < game->settings->width; x++) {
		*(current++) = '-';
	}
	*(current++) = '+';
	*(current++) = '\n';
	fprintf(stdout, "%s", buffer);
}

void inputValidatedMove(Game *game, int *move) {
	int x, y;
	x = -1;
	y = -1;
	while (x < 1 || x > game->settings->width || y < 1 || y > game->settings->height) {
		fprintf(stdout, "\nEnter move (x y): ");
		fscanf(stdin, "%d %d", &x, &y);
		if (getPlayerIndex(game, x, y) == 0) {
			move[0] = x;
			move[1] = y;
			return;
		}
	}
}

void playHotseat(Game * game) {
	int *move = calloc(2, sizeof(int));
	draw(game);
	inputValidatedMove(game, move);
	fprintf(stdout, "You entered move (%d %d)\n", move[0], move[1]);
	free(move);
}

int main(int argc, char** argv) {
	Settings * defaultSettings;
	defaultSettings = newSettings(2, 3, 3, " OX");
	Game *gameBoard = NULL;
	while (1) {
		int menu = menuMain();
		switch(menu) {
		case 1:
			gameBoard = newGame(defaultSettings);
			playHotseat(gameBoard);
			break;
		case 2:
			fprintf(stdout, "Starting new AI game\n");
			break;
		case 3:
			fprintf(stdout, "Configure game settings\n");
			break;
		case 4:
			fprintf(stdout, "List high scores\n");
			break;
		case 5:
			fprintf(stdout, "Exiting");
			return 0;
		}
	}
	return 0;
}

