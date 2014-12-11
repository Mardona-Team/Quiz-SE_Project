class CreateGroups < ActiveRecord::Migration
  def change
    create_table :groups do |t|
      t.integer :instructor_id
      t.string :group_name
      t.string :title
      t.string :year
      t.string :subject
      t.string :description

      t.timestamps
    end
  end
end
