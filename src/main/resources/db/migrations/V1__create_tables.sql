CREATE TABLE IF NOT EXISTS Users
(
    user_id    BIGINT                              NOT NULL,
    email      VARCHAR(255) UNIQUE                 NOT NULL,
    password   VARCHAR(255)                        NOT NULL,
    username   VARCHAR(255) UNIQUE                 NOT NULL,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    is_admin   BOOLEAN   DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT user_pk PRIMARY KEY (user_id)
);

ALTER TABLE Users
    ADD CONSTRAINT ck_users_email_validate CHECK ( email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$' );

CREATE TABLE Recipes
(
    recipe_id   BIGINT       NOT NULL,
    user_id     INT          NOT NULL,
    title       VARCHAR(255) NOT NULL,
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

CREATE TABLE AllergyWarnings
(
    allergy_id   BIGINT       NOT NULL,
    allergy_name VARCHAR(255) NOT NULL,
    description  TEXT,
    allergens    TEXT,
    CONSTRAINT allergy_waking_pk PRIMARY KEY (allergy_id)
);

CREATE TABLE RecipeAllergyWarnings
(
    allergy_id INT,
    recipe_id  INT,
    CONSTRAINT recipe_allergy_warning_pk PRIMARY KEY (allergy_id, recipe_id),
    CONSTRAINT recipe_allergy_fk FOREIGN KEY (allergy_id) REFERENCES AllergyWarnings (allergy_id),
    CONSTRAINT recipe_allergy_recipe_fk FOREIGN KEY (recipe_id) REFERENCES Recipes (recipe_id)
);

CREATE TABLE Ingredients
(
    ingredient_id    BIGINT       NOT NULL,
    recipe_id        INT,
    ingredient_name  VARCHAR(255) NOT NULL,
    quantity         DECIMAL(10, 2),
    unit             VARCHAR(50),
    ingredient_image VARCHAR(255),
    CONSTRAINT ingredients_pk PRIMARY KEY (ingredient_id),
    CONSTRAINT ingredients_recipe_fk FOREIGN KEY (recipe_id) REFERENCES Recipes (recipe_id)
);

CREATE TABLE Categories
(
    category_id   BIGINT       NOT NULL,
    category_name VARCHAR(255) NOT NULL,
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

CREATE TABLE Reviews
(
    review_id BIGINT NOT NULL,
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
    user_id BIGINT NOT NULL,
    note    TEXT,
    CONSTRAINT wishlist_pk PRIMARY KEY (user_id),
    CONSTRAINT wishlist_users_fk FOREIGN KEY (user_id) REFERENCES Users (user_id)
);