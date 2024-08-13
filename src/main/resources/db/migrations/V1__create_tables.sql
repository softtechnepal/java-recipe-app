CREATE SEQUENCE IF NOT EXISTS user_id_seq START 100 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS Users
(
    user_id    BIGINT    DEFAULT NEXTVAL('user_id_seq') NOT NULL,
    email      VARCHAR(255) UNIQUE                      NOT NULL,
    password   VARCHAR(255)                             NOT NULL,
    username   VARCHAR(255) UNIQUE                      NOT NULL,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    is_admin   BOOLEAN   DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP      NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT user_pk PRIMARY KEY (user_id)
);

ALTER TABLE Users
    ADD CONSTRAINT ck_users_email_validate CHECK ( email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$' );


CREATE SEQUENCE IF NOT EXISTS recipe_id_seq START 100 INCREMENT BY 1;

CREATE TABLE Recipes
(
    recipe_id   BIGINT    DEFAULT NEXTVAL('recipe_id_seq') NOT NULL,
    user_id     INT                                        NOT NULL,
    title       VARCHAR(255)                               NOT NULL,
    description TEXT,
    image       VARCHAR(255),
    video_url   VARCHAR(255),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT recipes_pk PRIMARY KEY (recipe_id),
    CONSTRAINT recipes_user_fk FOREIGN KEY (user_id) REFERENCES Users (user_id)
);

CREATE TABLE NutritionalInformation
(
    recipe_id     BIGINT NOT NULL,
    calories      INT,
    protein       DECIMAL(5, 2),
    fat           DECIMAL(5, 2),
    carbohydrates DECIMAL(5, 2),
    other         TEXT,
    CONSTRAINT nutritional_information_pk PRIMARY KEY (recipe_id),
    CONSTRAINT nutritional_info_recipe_fk FOREIGN KEY (recipe_id) REFERENCES Recipes (recipe_id)
);

CREATE SEQUENCE IF NOT EXISTS ingredient_id_seq START 100 INCREMENT BY 1;
CREATE TABLE Ingredients
(
    ingredient_id    BIGINT DEFAULT NEXTVAL('ingredient_id_seq') NOT NULL,
    recipe_id        INT,
    ingredient_name  VARCHAR(255)                                NOT NULL,
    quantity         DECIMAL(10, 2),
    unit             VARCHAR(50),
    ingredient_image VARCHAR(255),
    CONSTRAINT ingredients_pk PRIMARY KEY (ingredient_id),
    CONSTRAINT ingredients_recipe_fk FOREIGN KEY (recipe_id) REFERENCES Recipes (recipe_id)
);

CREATE SEQUENCE IF NOT EXISTS category_id_seq START 100 INCREMENT BY 1;
CREATE TABLE Categories
(
    category_id   BIGINT DEFAULT NEXTVAL('category_id_seq') NOT NULL,
    category_name VARCHAR(255)                              NOT NULL,
    CONSTRAINT categories_pk PRIMARY KEY (category_id)
);

CREATE TABLE RecipeCategories
(
    recipe_id   INT,
    category_id INT,
    CONSTRAINT recipe_categories_pk PRIMARY KEY (recipe_id, category_id),
    CONSTRAINT recipe_category_recipe_fk FOREIGN KEY (recipe_id) REFERENCES Recipes (recipe_id),
    CONSTRAINT recipe_category_category_fk FOREIGN KEY (category_id) REFERENCES Categories (category_id)
);

CREATE SEQUENCE IF NOT EXISTS review_id_seq START 100 INCREMENT BY 1;
CREATE TABLE Reviews
(
    review_id BIGINT DEFAULT NEXTVAL('review_id_seq') NOT NULL,
    user_id   INT,
    recipe_id INT,
    rating    INT CHECK (rating >= 1 AND rating <= 5),
    review    TEXT,
    CONSTRAINT review_pk PRIMARY KEY (review_id),
    CONSTRAINT review_users_fk FOREIGN KEY (user_id) REFERENCES Users (user_id),
    CONSTRAINT review_recipe_fk FOREIGN KEY (recipe_id) REFERENCES Recipes (recipe_id)
);

CREATE TABLE WishList
(
    user_id   BIGINT NOT NULL,
    recipe_id BIGINT NOT NULL,
    note      TEXT,
    CONSTRAINT wishlist_pk PRIMARY KEY (user_id, recipe_id),
    CONSTRAINT wishlist_users_fk FOREIGN KEY (user_id) REFERENCES Users (user_id),
    CONSTRAINT wishlist_recipe_fk FOREIGN KEY (recipe_id) REFERENCES Recipes (recipe_id)
);