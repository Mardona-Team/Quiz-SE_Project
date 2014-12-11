# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20141211134046) do

  create_table "answers", force: true do |t|
    t.integer  "question_id"
    t.string   "title"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "answers", ["question_id"], name: "index_answers_on_question_id", using: :btree

  create_table "groups", force: true do |t|
    t.integer  "instructor_id"
    t.string   "group_name"
    t.string   "title"
    t.string   "year"
    t.string   "subject"
    t.string   "description"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "memberships", force: true do |t|
    t.boolean  "status"
    t.integer  "student_id"
    t.integer  "group_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "questions", force: true do |t|
    t.integer  "quiz_id"
    t.string   "title"
    t.integer  "right_answer_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "questions", ["quiz_id"], name: "index_questions_on_quiz_id", using: :btree

  create_table "quizzes", force: true do |t|
    t.integer  "group_id"
    t.integer  "instructor_id"
    t.string   "title"
    t.string   "subject"
    t.string   "year"
    t.string   "description"
    t.integer  "marks"
    t.boolean  "status"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "quizzes", ["group_id"], name: "index_quizzes_on_group_id", using: :btree

  create_table "users", force: true do |t|
    t.string   "username",   null: false
    t.string   "first_name", null: false
    t.string   "last_name",  null: false
    t.string   "password"
    t.string   "email"
    t.string   "type"
    t.string   "faculty"
    t.string   "university"
    t.string   "department"
    t.string   "year"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

end
