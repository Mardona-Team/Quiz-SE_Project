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

ActiveRecord::Schema.define(version: 20150203011918) do

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

  create_table "students_answers", id: false, force: true do |t|
    t.integer "student_id"
    t.integer "answer_id"
  end

  add_index "students_answers", ["student_id", "answer_id"], name: "index_students_answers_on_student_id_and_answer_id", using: :btree

  create_table "users", force: true do |t|
    t.string   "username",                            null: false
    t.string   "first_name",                          null: false
    t.string   "last_name",                           null: false
    t.string   "type"
    t.string   "faculty"
    t.string   "university"
    t.string   "department"
    t.string   "year"
    t.string   "email",                  default: "", null: false
    t.string   "encrypted_password",     default: "", null: false
    t.string   "reset_password_token"
    t.datetime "reset_password_sent_at"
    t.datetime "remember_created_at"
    t.integer  "sign_in_count",          default: 0,  null: false
    t.datetime "current_sign_in_at"
    t.datetime "last_sign_in_at"
    t.string   "current_sign_in_ip"
    t.string   "last_sign_in_ip"
    t.string   "confirmation_token"
    t.datetime "confirmed_at"
    t.datetime "confirmation_sent_at"
    t.string   "unconfirmed_email"
    t.string   "authentication_token"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "users", ["authentication_token"], name: "index_users_on_authentication_token", unique: true, using: :btree
  add_index "users", ["confirmation_token"], name: "index_users_on_confirmation_token", unique: true, using: :btree
  add_index "users", ["email"], name: "index_users_on_email", unique: true, using: :btree
  add_index "users", ["reset_password_token"], name: "index_users_on_reset_password_token", unique: true, using: :btree

end
