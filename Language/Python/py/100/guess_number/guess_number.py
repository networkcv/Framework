import random

word_list = ["jack", "rose", "gog", "bob"]


def chose_word(cur_word_list):
    if len(cur_word_list) == 0:
        return ""
    return random.choice(cur_word_list)


word = chose_word(word_list)
print("lets play a guess game!")
guess_record = []
for x in range(len(word)):
    guess_record.append("_")
guess_result = False
guess_error_num = 0
max_guess_error_num = 3


def find_in_word(letter, word_, guess_recode):
    for i in range(len(word_)):
        if word_[i] == letter and guess_recode[i] == '_':
            return i
    return -1


while guess_error_num < max_guess_error_num and not guess_result:
    for x in guess_record:
        print(x, end="")
    print()
    guess_word = input("input your guessed word:\n")
    index = find_in_word(guess_word, word, guess_record)
    if index != -1:
        guess_record[index] = guess_word
        if list(word) == guess_record:
            guess_result = True
    else:
        guess_error_num += 1
        print(f"error {guess_error_num} time")

if guess_result:
    print("you win!")
else:
    print("you lose!")
