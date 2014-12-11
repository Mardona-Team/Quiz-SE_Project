class CreateQuestions < ActiveRecord::Migration
  def change
    create_table :questions do |t|
      t.string :title
      t.integer :right_answer_id

      t.timestamps
    end
  end
end
