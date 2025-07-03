use `note-share`;
-- Bảng Users: Quản lý thông tin người dùng
CREATE TABLE `ums_user` (
                            `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                            `username` VARCHAR(255) NOT NULL,
                            `email` VARCHAR(255) UNIQUE NOT NULL,
                            `status` INT(1),
                            `password` VARCHAR(255) NOT NULL,
                            `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Bảng Workspaces: Quản lý không gian làm việc
CREATE TABLE `nms_workspace` (
                                 `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 `name` VARCHAR(255) NOT NULL,
                                 `description` TEXT,
                                 `created_by` BIGINT,
                                 `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 FOREIGN KEY (`created_by`) REFERENCES `ums_user`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB;

-- Bảng Workspace_Members: Quản lý thành viên trong không gian làm việc
CREATE TABLE `nms_workspace_member` (
                                        `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        `workspace_id` BIGINT,
                                        `user_id` BIGINT,
                                        `role` VARCHAR(50) NOT NULL,
                                        `joined_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        FOREIGN KEY (`workspace_id`) REFERENCES `nms_workspace`(`id`) ON DELETE CASCADE,
                                        FOREIGN KEY (`user_id`) REFERENCES `ums_user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Bảng Pages: Quản lý các trang
CREATE TABLE `pms_page` (
                            `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                            `workspace_id` BIGINT,
                            `parent_page_id` BIGINT NULL,
                            `title` VARCHAR(255) NOT NULL,
                            `created_by` BIGINT,
                            `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            FOREIGN KEY (`workspace_id`) REFERENCES `nms_workspace`(`id`) ON DELETE CASCADE,
                            FOREIGN KEY (`parent_page_id`) REFERENCES `pms_page`(`id`) ON DELETE SET NULL,
                            FOREIGN KEY (`created_by`) REFERENCES `ums_user`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB;

-- Bảng Blocks: Quản lý khối nội dung trong trang
CREATE TABLE `pms_block` (
                             `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                             `page_id` BIGINT,
                             `type` VARCHAR(50) NOT NULL,
                             `content` TEXT,
                             `order` INT NOT NULL,
                             `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             FOREIGN KEY (`page_id`) REFERENCES `pms_page`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE `ums_role`(
                           `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                           `name` VARCHAR(255),
                           `status` int(1),
                           `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;
CREATE TABLE `ums_role_resource_relation`(
                                             `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             `role_id` BIGINT,
                                             `resource_id` BIGINT,
                                             FOREIGN KEY (`role_id`) REFERENCES `ums_role`(`id`),
) ENGINE=InnoDB;

-- Bảng Invitations: Quản lý lời mời để chia sẻ
CREATE TABLE `ums_invitation` (
                                  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  `inviter_id` BIGINT,
                                  `invitee_email` VARCHAR(255) NOT NULL,
                                  `object_type` VARCHAR(50) NOT NULL,
                                  `object_id` INT NOT NULL,
                                  `role` VARCHAR(50) NOT NULL,
                                  `status` VARCHAR(50) DEFAULT 'pending',
                                  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  FOREIGN KEY (`inviter_id`) REFERENCES `ums_user`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB;
