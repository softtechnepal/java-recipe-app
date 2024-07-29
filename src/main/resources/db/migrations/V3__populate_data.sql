/*Populate Users Data*/
INSERT INTO Users (email, password, username, first_name, last_name, is_admin)
VALUES
    ('prachan.ghale@example.com', 'password123', 'prachanghale', 'Prachan', 'Ghale', TRUE),
    ('sandip.moktan@example.com', 'password123', 'sandipmoktan', 'Sandip', 'Moktan', FALSE),
    ('anchal.gurung@example.com', 'password123', 'anchalgurung', 'Anchal', 'Gurung', FALSE),
    ('nigam.bhattarai@example.com', 'password123', 'nigambhattarai', 'Nigam', 'Bhattarai', FALSE),
    ('alisha.shrestha@example.com', 'password123', 'alishashrestha', 'Alisha', 'Shrestha', FALSE);

-- Populate Categories
INSERT INTO Categories (category_name) VALUES
                                           ('Appetizers'),
                                           ('Main Courses'),
                                           ('Desserts'),
                                           ('Beverages'),
                                           ('Snacks');

-- Populate Recipe
INSERT INTO Recipes (user_id, title, description, image, video_url, created_at, updated_at)
VALUES
    (101, 'Spaghetti Carbonara', 'A classic Italian pasta dish made with eggs, cheese, pancetta, and pepper.', 'carbonara.jpg', 'https://example.com/carbonara', NOW(), NOW()),
    (102, 'Chicken Alfredo', 'Creamy pasta dish with chicken and parmesan cheese.', 'alfredo.jpg', 'https://example.com/alfredo', NOW(), NOW()),
    (101, 'Beef Stroganoff', 'Tender beef strips in a creamy mushroom sauce.', 'stroganoff.jpg', 'https://example.com/stroganoff', NOW(), NOW()),
    (102, 'Vegetable Stir Fry', 'A mix of fresh vegetables stir-fried with soy sauce and garlic.', 'stirfry.jpg', 'https://example.com/stirfry', NOW(), NOW()),
    (101, 'Margherita Pizza', 'Classic pizza topped with tomato sauce, mozzarella cheese, and basil.', 'pizza.jpg', 'https://example.com/pizza', NOW(), NOW()),
    (102, 'Caesar Salad', 'Crisp romaine lettuce with Caesar dressing, croutons, and parmesan cheese.', 'caesar.jpg', 'https://example.com/caesar', NOW(), NOW()),
    (101, 'Beef Tacos', 'Tacos filled with seasoned ground beef, lettuce, and cheese.', 'tacos.jpg', 'https://example.com/tacos', NOW(), NOW()),
    (102, 'Chicken Curry', 'Spicy and flavorful chicken curry with a blend of Indian spices.', 'curry.jpg', 'https://example.com/curry', NOW(), NOW()),
    (101, 'Greek Salad', 'A fresh salad with cucumbers, tomatoes, olives, feta cheese, and red onions.', 'greeksalad.jpg', 'https://example.com/greeksalad', NOW(), NOW()),
    (102, 'Lemon Drizzle Cake', 'A moist cake with a tangy lemon glaze.', 'lemoncake.jpg', 'https://example.com/lemoncake', NOW(), NOW()),
    (101, 'Pancakes', 'Fluffy pancakes served with maple syrup and butter.', 'pancakes.jpg', 'https://example.com/pancakes', NOW(), NOW()),
    (102, 'Lasagna', 'Layers of pasta, meat sauce, and cheese baked to perfection.', 'lasagna.jpg', 'https://example.com/lasagna', NOW(), NOW()),
    (101, 'Vegetable Soup', 'A hearty soup filled with various fresh vegetables.', 'soup.jpg', 'https://example.com/soup', NOW(), NOW()),
    (102, 'BBQ Ribs', 'Tender ribs cooked with a smoky BBQ sauce.', 'ribs.jpg', 'https://example.com/ribs', NOW(), NOW()),
    (101, 'Apple Pie', 'Classic apple pie with a buttery crust and sweet apple filling.', 'applepie.jpg', 'https://example.com/applepie', NOW(), NOW()),
    (102, 'Fish Tacos', 'Tacos made with seasoned fish, cabbage slaw, and a creamy sauce.', 'fishtacos.jpg', 'https://example.com/fishtacos', NOW(), NOW()),
    (101, 'Quiche Lorraine', 'Savory pie filled with bacon, eggs, and cheese.', 'quiche.jpg', 'https://example.com/quiche', NOW(), NOW()),
    (102, 'Stuffed Bell Peppers', 'Bell peppers filled with a mixture of ground meat, rice, and vegetables.', 'stuffedpeppers.jpg', 'https://example.com/stuffedpeppers', NOW(), NOW()),
    (101, 'Chicken Caesar Wrap', 'Grilled chicken wrapped with Caesar salad ingredients.', 'wrap.jpg', 'https://example.com/wrap', NOW(), NOW()),
    (102, 'Brownies', 'Fudgy chocolate brownies with a crispy top.', 'brownies.jpg', 'https://example.com/brownies', NOW(), NOW()),
    (101, 'Mushroom Risotto', 'Creamy risotto with sautéed mushrooms and parmesan cheese.', 'risotto.jpg', 'https://example.com/risotto', NOW(), NOW()),
    (102, 'Chili Con Carne', 'Spicy beef chili with beans and tomatoes.', 'chili.jpg', 'https://example.com/chili', NOW(), NOW());