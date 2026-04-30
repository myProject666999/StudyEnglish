-- 创建数据库
CREATE DATABASE IF NOT EXISTS study_english DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE study_english;

-- 用户表
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    nickname VARCHAR(50),
    avatar VARCHAR(255),
    role TINYINT DEFAULT 0 COMMENT '0-普通用户, 1-管理员',
    status TINYINT DEFAULT 1 COMMENT '0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 单词等级表
CREATE TABLE IF NOT EXISTS t_word_level (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL COMMENT '等级名称',
    description VARCHAR(255) COMMENT '描述',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='单词等级表';

-- 单词表
CREATE TABLE IF NOT EXISTS t_word (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    level_id BIGINT NOT NULL COMMENT '单词等级ID',
    word VARCHAR(100) NOT NULL COMMENT '单词',
    phonetic VARCHAR(100) COMMENT '音标',
    meaning TEXT COMMENT '释义',
    example TEXT COMMENT '例句',
    example_meaning TEXT COMMENT '例句翻译',
    audio_url VARCHAR(255) COMMENT '音频URL',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='单词表';

-- 书籍表
CREATE TABLE IF NOT EXISTS t_book (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL COMMENT '标题',
    author VARCHAR(100) COMMENT '作者',
    description TEXT COMMENT '描述',
    cover_url VARCHAR(255) COMMENT '封面URL',
    category VARCHAR(50) COMMENT '分类',
    difficulty TINYINT DEFAULT 1 COMMENT '难度等级',
    status TINYINT DEFAULT 1 COMMENT '0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍表';

-- 阅读文章表
CREATE TABLE IF NOT EXISTS t_article (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT COMMENT '书籍ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    translation TEXT COMMENT '翻译',
    difficulty TINYINT DEFAULT 1 COMMENT '难度等级',
    word_count INT DEFAULT 0 COMMENT '字数',
    audio_url VARCHAR(255) COMMENT '音频URL',
    status TINYINT DEFAULT 1 COMMENT '0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='阅读文章表';

-- 听力材料表
CREATE TABLE IF NOT EXISTS t_listening (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL COMMENT '标题',
    description TEXT COMMENT '描述',
    audio_url VARCHAR(255) NOT NULL COMMENT '音频URL',
    transcript TEXT COMMENT '原文',
    translation TEXT COMMENT '翻译',
    difficulty TINYINT DEFAULT 1 COMMENT '难度等级',
    duration INT DEFAULT 0 COMMENT '时长(秒)',
    status TINYINT DEFAULT 1 COMMENT '0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='听力材料表';

-- 题目表
CREATE TABLE IF NOT EXISTS t_question (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_type TINYINT NOT NULL COMMENT '题目类型: 1-单词, 2-阅读, 3-听力',
    question TEXT NOT NULL COMMENT '题目',
    options TEXT COMMENT '选项(JSON格式)',
    answer VARCHAR(200) NOT NULL COMMENT '答案',
    analysis TEXT COMMENT '解析',
    difficulty TINYINT DEFAULT 1 COMMENT '难度等级',
    word_id BIGINT COMMENT '关联单词ID',
    article_id BIGINT COMMENT '关联文章ID',
    listening_id BIGINT COMMENT '关联听力ID',
    status TINYINT DEFAULT 1 COMMENT '0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题目表';

-- 名人名言表
CREATE TABLE IF NOT EXISTS t_quote (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL COMMENT '内容(英文)',
    content_cn TEXT COMMENT '内容(中文)',
    author VARCHAR(100) COMMENT '作者',
    source VARCHAR(200) COMMENT '来源',
    is_daily TINYINT DEFAULT 0 COMMENT '是否每日一句: 0-否, 1-是',
    daily_date DATE COMMENT '每日日期',
    status TINYINT DEFAULT 1 COMMENT '0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='名人名言表';

-- 用户学习进度表
CREATE TABLE IF NOT EXISTS t_user_progress (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    total_words INT DEFAULT 0 COMMENT '总学习单词数',
    learned_words INT DEFAULT 0 COMMENT '已学习单词数',
    mastered_words INT DEFAULT 0 COMMENT '已掌握单词数',
    total_articles INT DEFAULT 0 COMMENT '总阅读文章数',
    read_articles INT DEFAULT 0 COMMENT '已阅读文章数',
    total_listenings INT DEFAULT 0 COMMENT '总听力数',
    completed_listenings INT DEFAULT 0 COMMENT '已完成听力数',
    total_questions INT DEFAULT 0 COMMENT '总做题数',
    correct_questions INT DEFAULT 0 COMMENT '正确数',
    study_days INT DEFAULT 0 COMMENT '学习天数',
    continuous_days INT DEFAULT 0 COMMENT '连续学习天数',
    last_study_date DATE COMMENT '最后学习日期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户学习进度表';

-- 学习记录表
CREATE TABLE IF NOT EXISTS t_study_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    record_type TINYINT NOT NULL COMMENT '记录类型: 1-单词, 2-阅读, 3-听力, 4-题目',
    type_id BIGINT COMMENT '关联类型ID',
    content VARCHAR(500) COMMENT '内容概要',
    is_correct TINYINT COMMENT '是否正确(题目)',
    study_time INT DEFAULT 0 COMMENT '学习时长(秒)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习记录表';

-- 用户单词学习记录表
CREATE TABLE IF NOT EXISTS t_user_word (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    word_id BIGINT NOT NULL COMMENT '单词ID',
    is_new TINYINT DEFAULT 1 COMMENT '是否生词: 0-否, 1-是',
    is_learned TINYINT DEFAULT 0 COMMENT '是否已学: 0-否, 1-是',
    is_mastered TINYINT DEFAULT 0 COMMENT '是否已掌握: 0-否, 1-是',
    review_count INT DEFAULT 0 COMMENT '复习次数',
    last_review_time DATETIME COMMENT '最后复习时间',
    next_review_time DATETIME COMMENT '下次复习时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_word (user_id, word_id),
    INDEX idx_user_id (user_id),
    INDEX idx_next_review (next_review_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户单词学习记录表';

-- 用户题目记录表
CREATE TABLE IF NOT EXISTS t_user_question (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    question_id BIGINT NOT NULL COMMENT '题目ID',
    is_correct TINYINT DEFAULT 0 COMMENT '是否正确: 0-错误, 1-正确',
    is_wrong_book TINYINT DEFAULT 0 COMMENT '是否加入错题本: 0-否, 1-是',
    user_answer VARCHAR(200) COMMENT '用户答案',
    attempt_count INT DEFAULT 1 COMMENT '尝试次数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_question_id (question_id),
    INDEX idx_wrong_book (user_id, is_wrong_book)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户题目记录表';

-- 初始化管理员账号 (密码: admin123)
INSERT INTO t_user (username, password, nickname, role, status) VALUES 
('admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', 1, 1);

-- 初始化单词等级
INSERT INTO t_word_level (name, description, sort, status) VALUES 
('入门级', '适合英语初学者', 1, 1),
('初级', '适合英语初级学习者', 2, 1),
('中级', '适合英语中级学习者', 3, 1),
('高级', '适合英语高级学习者', 4, 1),
('专业级', '适合英语专业学习者', 5, 1);

-- 初始化名人名言
INSERT INTO t_quote (content, content_cn, author, is_daily, status) VALUES 
('Where there is a will, there is a way.', '有志者，事竟成。', '佚名', 1, 1),
('Genius is one percent inspiration and ninety-nine percent perspiration.', '天才是百分之一的灵感加百分之九十九的汗水。', '爱迪生', 0, 1),
('Knowledge is power.', '知识就是力量。', '培根', 0, 1),
('Practice makes perfect.', '熟能生巧。', '佚名', 0, 1),
('The only way to do great work is to love what you do.', '做出伟大工作的唯一方法是热爱你所做的事。', '乔布斯', 0, 1);
