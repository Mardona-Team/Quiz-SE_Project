class CreateQuizzes < ActiveRecord::Migration
  def change
    create_table :quizzes do |t|
      t.belongs_to :group, index: true
      t.integer :instructor_id
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
