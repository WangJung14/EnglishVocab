CREATE TABLE badges
(
    id              BINARY(16)   NOT NULL,
    name            VARCHAR(100) NOT NULL,
    `description`   TEXT NULL,
    icon_url        TEXT NULL,
    condition_type  VARCHAR(50) NULL,
    condition_value INT NULL,
    CONSTRAINT pk_badges PRIMARY KEY (id)
);

CREATE TABLE categories
(
    id            BINARY(16)   NOT NULL,
    name          VARCHAR(100) NOT NULL,
    slug          VARCHAR(100) NOT NULL,
    `description` TEXT NULL,
    icon_url      TEXT NULL,
    order_index   INT NULL,
    created_at    datetime NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE exercises
(
    id                 BINARY(16)   NOT NULL,
    lesson_id          BINARY(16)   NOT NULL,
    title              VARCHAR(255) NOT NULL,
    `description`      TEXT NULL,
    time_limit_seconds INT NULL,
    pass_score         INT NULL,
    order_index        INT NULL,
    created_by         BINARY(16)   NULL,
    created_at         datetime NULL,
    CONSTRAINT pk_exercises PRIMARY KEY (id)
);

CREATE TABLE flashcard_decks
(
    id            BINARY(16)   NOT NULL,
    title         VARCHAR(255) NOT NULL,
    `description` TEXT NULL,
    created_by    BINARY(16)   NOT NULL,
    is_public     BIT(1)       NOT NULL,
    lesson_id     BINARY(16)   NULL,
    created_at    datetime NULL,
    CONSTRAINT pk_flashcard_decks PRIMARY KEY (id)
);

CREATE TABLE flashcards
(
    id          BINARY(16) NOT NULL,
    deck_id     BINARY(16) NOT NULL,
    word_id     BINARY(16) NULL,
    front       TEXT NOT NULL,
    back        TEXT NOT NULL,
    order_index INT NULL,
    created_at  datetime NULL,
    CONSTRAINT pk_flashcards PRIMARY KEY (id)
);

CREATE TABLE lesson_words
(
    id          BINARY(16) NOT NULL,
    lesson_id   BINARY(16) NOT NULL,
    word_id     BINARY(16) NOT NULL,
    order_index INT NULL,
    CONSTRAINT pk_lesson_words PRIMARY KEY (id)
);

CREATE TABLE lessons
(
    id           BINARY(16)   NOT NULL,
    topic_id     BINARY(16)   NOT NULL,
    slug         VARCHAR(255) NOT NULL,
    lesson_type  VARCHAR(255) NOT NULL,
    content      TEXT NULL,
    summary      TEXT NULL,
    is_free      BIT(1)       NOT NULL,
    is_published BIT(1)       NOT NULL,
    order_index  INT NULL,
    created_by   BINARY(16)   NULL,
    created_at   datetime NULL,
    update_at    datetime NULL,
    CONSTRAINT pk_lessons PRIMARY KEY (id)
);

CREATE TABLE question_options
(
    id            BINARY(16) NOT NULL,
    question_id   BINARY(16) NOT NULL,
    content       TEXT NOT NULL,
    is_correct    BIT(1) NULL,
    match_pair_id BINARY(16) NULL,
    order_index   INT NULL,
    CONSTRAINT pk_question_options PRIMARY KEY (id)
);

CREATE TABLE questions
(
    id            BINARY(16)   NOT NULL,
    exercise_id   BINARY(16)   NOT NULL,
    question_type VARCHAR(255) NOT NULL,
    content       TEXT         NOT NULL,
    explanation   TEXT NULL,
    score_weight  INT NULL,
    order_index   INT NULL,
    created_at    datetime NULL,
    CONSTRAINT pk_questions PRIMARY KEY (id)
);

CREATE TABLE reading_passages
(
    id         BINARY(16)   NOT NULL,
    lesson_id  BINARY(16)   NOT NULL,
    title      VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    audio_url  TEXT NULL,
    source     VARCHAR(255) NULL,
    word_count INT NULL,
    created_at datetime NULL,
    CONSTRAINT pk_reading_passages PRIMARY KEY (id)
);

CREATE TABLE topics
(
    id            BINARY(16)   NOT NULL,
    category_id   BINARY(16)   NOT NULL,
    title         VARCHAR(255) NOT NULL,
    slug          VARCHAR(255) NOT NULL,
    `description` TEXT NULL,
    thumbnail_url TEXT NULL,
    level         VARCHAR(255) NOT NULL,
    is_published  BIT(1)       NOT NULL,
    order_index   INT NULL,
    created_by    BINARY(16)   NULL,
    created_at    datetime NULL,
    updated_at    datetime NULL,
    CONSTRAINT pk_topics PRIMARY KEY (id)
);

CREATE TABLE user_answer_logs
(
    id                 BINARY(16) NOT NULL,
    attempt_id         BINARY(16) NOT NULL,
    question_id        BINARY(16) NOT NULL,
    selected_option_id BINARY(16) NULL,
    text_answer        TEXT NULL,
    is_correct         BIT(1) NOT NULL,
    CONSTRAINT pk_user_answer_logs PRIMARY KEY (id)
);

CREATE TABLE user_badges
(
    id        BINARY(16) NOT NULL,
    user_id   BINARY(16) NOT NULL,
    badge_id  BINARY(16) NOT NULL,
    earned_at datetime NULL,
    CONSTRAINT pk_user_badges PRIMARY KEY (id)
);

CREATE TABLE user_exercise_attempts
(
    id                 BINARY(16) NOT NULL,
    user_id            BINARY(16) NOT NULL,
    exercise_id        BINARY(16) NOT NULL,
    score DOUBLE NOT NULL,
    max_score DOUBLE NOT NULL,
    is_passed          BIT(1) NOT NULL,
    time_spent_seconds INT NULL,
    attempted_at       datetime NULL,
    CONSTRAINT pk_user_exercise_attempts PRIMARY KEY (id)
);

CREATE TABLE user_flashcard_srs
(
    id               BINARY(16) NOT NULL,
    user_id          BINARY(16) NOT NULL,
    flashcard_id     BINARY(16) NOT NULL,
    repetition       INT NOT NULL,
    interval_days    INT NOT NULL,
    ease_factor DOUBLE NOT NULL,
    next_review_at   datetime NULL,
    last_reviewed_at datetime NULL,
    total_reviews    INT NULL,
    CONSTRAINT pk_user_flashcard_srs PRIMARY KEY (id)
);

CREATE TABLE user_lesson_progress
(
    id               BINARY(16)   NOT NULL,
    user_id          BINARY(16)   NOT NULL,
    lesson_id        BINARY(16)   NOT NULL,
    status           VARCHAR(255) NOT NULL,
    progress_percent INT NULL,
    completed_at     datetime NULL,
    updated_at       datetime NULL,
    CONSTRAINT pk_user_lesson_progress PRIMARY KEY (id)
);

CREATE TABLE user_streaks
(
    user_id            BINARY(16) NOT NULL,
    current_streak     INT NOT NULL,
    longest_streak     INT NOT NULL,
    last_activity_date date NULL,
    CONSTRAINT pk_user_streaks PRIMARY KEY (user_id)
);

CREATE TABLE users
(
    id                    BINARY(16)   NOT NULL,
    email                 VARCHAR(255) NOT NULL,
    password_hash         VARCHAR(255) NULL,
    first_name            VARCHAR(50) NULL,
    last_name             VARCHAR(50) NULL,
    avatar_url            TEXT NULL,
    `role`                VARCHAR(255) NOT NULL,
    membership_type       VARCHAR(255) NOT NULL,
    membership_expires_at datetime NULL,
    is_active             BIT(1)       NOT NULL,
    created_at            datetime NULL,
    updated_at            datetime NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE words
(
    id                  BINARY(16)   NOT NULL,
    word                VARCHAR(100) NOT NULL,
    phonetic            VARCHAR(100) NULL,
    audio_url           TEXT NULL,
    part_of_speech      VARCHAR(255) NULL,
    definition_vi       TEXT NULL,
    example_sentence    TEXT NULL,
    example_transaction TEXT NULL,
    image_url           TEXT NULL,
    level               VARCHAR(255) NULL,
    created_by          BINARY(16)   NULL,
    created_at          datetime NULL,
    CONSTRAINT pk_words PRIMARY KEY (id)
);

ALTER TABLE user_lesson_progress
    ADD CONSTRAINT uc_4be6ec0161ccfb76815e2ae9f UNIQUE (user_id, lesson_id);

ALTER TABLE user_flashcard_srs
    ADD CONSTRAINT uc_543c65d305e6cc321985dd260 UNIQUE (user_id, flashcard_id);

ALTER TABLE categories
    ADD CONSTRAINT uc_categories_slug UNIQUE (slug);

ALTER TABLE lessons
    ADD CONSTRAINT uc_lessons_slug UNIQUE (slug);

ALTER TABLE reading_passages
    ADD CONSTRAINT uc_reading_passages_lesson UNIQUE (lesson_id);

ALTER TABLE topics
    ADD CONSTRAINT uc_topics_slug UNIQUE (slug);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE exercises
    ADD CONSTRAINT FK_EXERCISES_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES users (id);

ALTER TABLE exercises
    ADD CONSTRAINT FK_EXERCISES_ON_LESSON FOREIGN KEY (lesson_id) REFERENCES lessons (id);

ALTER TABLE flashcards
    ADD CONSTRAINT FK_FLASHCARDS_ON_DECK FOREIGN KEY (deck_id) REFERENCES flashcard_decks (id);

ALTER TABLE flashcards
    ADD CONSTRAINT FK_FLASHCARDS_ON_WORD FOREIGN KEY (word_id) REFERENCES words (id);

ALTER TABLE flashcard_decks
    ADD CONSTRAINT FK_FLASHCARD_DECKS_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES users (id);

ALTER TABLE flashcard_decks
    ADD CONSTRAINT FK_FLASHCARD_DECKS_ON_LESSON FOREIGN KEY (lesson_id) REFERENCES lessons (id);

ALTER TABLE lessons
    ADD CONSTRAINT FK_LESSONS_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES users (id);

ALTER TABLE lessons
    ADD CONSTRAINT FK_LESSONS_ON_TOPIC FOREIGN KEY (topic_id) REFERENCES topics (id);

ALTER TABLE lesson_words
    ADD CONSTRAINT FK_LESSON_WORDS_ON_LESSON FOREIGN KEY (lesson_id) REFERENCES lessons (id);

ALTER TABLE lesson_words
    ADD CONSTRAINT FK_LESSON_WORDS_ON_WORD FOREIGN KEY (word_id) REFERENCES words (id);

ALTER TABLE questions
    ADD CONSTRAINT FK_QUESTIONS_ON_EXERCISE FOREIGN KEY (exercise_id) REFERENCES exercises (id);

ALTER TABLE question_options
    ADD CONSTRAINT FK_QUESTION_OPTIONS_ON_QUESTION FOREIGN KEY (question_id) REFERENCES questions (id);

ALTER TABLE reading_passages
    ADD CONSTRAINT FK_READING_PASSAGES_ON_LESSON FOREIGN KEY (lesson_id) REFERENCES lessons (id);

ALTER TABLE topics
    ADD CONSTRAINT FK_TOPICS_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id);

ALTER TABLE topics
    ADD CONSTRAINT FK_TOPICS_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES users (id);

ALTER TABLE user_answer_logs
    ADD CONSTRAINT FK_USER_ANSWER_LOGS_ON_ATTEMPT FOREIGN KEY (attempt_id) REFERENCES user_exercise_attempts (id);

ALTER TABLE user_answer_logs
    ADD CONSTRAINT FK_USER_ANSWER_LOGS_ON_QUESTION FOREIGN KEY (question_id) REFERENCES questions (id);

ALTER TABLE user_answer_logs
    ADD CONSTRAINT FK_USER_ANSWER_LOGS_ON_SELECTED_OPTION FOREIGN KEY (selected_option_id) REFERENCES question_options (id);

ALTER TABLE user_badges
    ADD CONSTRAINT FK_USER_BADGES_ON_BADGE FOREIGN KEY (badge_id) REFERENCES badges (id);

ALTER TABLE user_badges
    ADD CONSTRAINT FK_USER_BADGES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_exercise_attempts
    ADD CONSTRAINT FK_USER_EXERCISE_ATTEMPTS_ON_EXERCISE FOREIGN KEY (exercise_id) REFERENCES exercises (id);

ALTER TABLE user_exercise_attempts
    ADD CONSTRAINT FK_USER_EXERCISE_ATTEMPTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_flashcard_srs
    ADD CONSTRAINT FK_USER_FLASHCARD_SRS_ON_FLASHCARD FOREIGN KEY (flashcard_id) REFERENCES flashcards (id);

ALTER TABLE user_flashcard_srs
    ADD CONSTRAINT FK_USER_FLASHCARD_SRS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_lesson_progress
    ADD CONSTRAINT FK_USER_LESSON_PROGRESS_ON_LESSON FOREIGN KEY (lesson_id) REFERENCES lessons (id);

ALTER TABLE user_lesson_progress
    ADD CONSTRAINT FK_USER_LESSON_PROGRESS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_streaks
    ADD CONSTRAINT FK_USER_STREAKS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE words
    ADD CONSTRAINT FK_WORDS_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES users (id);