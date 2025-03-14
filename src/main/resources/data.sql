-- Insert ocean floor grid (5x3 representation)
INSERT INTO ocean_floor (x_position, y_position, is_obstacle) VALUES
    (0, 0, FALSE), (1, 0, FALSE), (2, 0, TRUE),  (3, 0, FALSE), (4, 0, FALSE),
    (0, 1, FALSE), (1, 1, TRUE),  (2, 1, FALSE), (3, 1, TRUE),  (4, 1, FALSE),
    (0, 2, FALSE), (1, 2, FALSE), (2, 2, FALSE), (3, 2, TRUE),  (4, 2, FALSE);