CREATE TABLE `user` (
                        `id` bigint NOT NULL,
                        `email` varchar(50) NOT NULL,
                        `password` varchar(50) NOT NULL,
                        `user_name` varchar(50) NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `post` (
                        `id` bigint NOT NULL,
                        `user_id` bigint NOT NULL,
                        `title` varchar(255) NOT NULL,
                        `content` text NOT NULL,
                        `thumbnail` varchar(255) NOT NULL,
                        `created_at` datetime DEFAULT NULL,
                        `updated_at` datetime DEFAULT NULL,
                        PRIMARY KEY (`id`),
                        KEY `user_id` (`user_id`),
                        CONSTRAINT `post_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;