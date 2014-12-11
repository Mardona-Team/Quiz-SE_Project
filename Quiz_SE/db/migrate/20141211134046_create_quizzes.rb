class CreateQuizzes < ActiveRecord::Migration
  def change
    create_table :quizzes do |t|
      t.string :title
      t.string :subject
      t.string :year
      t.string :description
      t.integer :marks
      t.boolean :status

      t.timestamps
    end
  end
end
