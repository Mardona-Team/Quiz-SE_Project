class CreateUsers < ActiveRecord::Migration
  def change
    create_table :users do |t|
      t.string :username , null: false
      t.string :first_name , null: false
      t.string :last_name , null: false
      t.string :password
      t.string :email
      t.string :type
      t.string :faculty
      t.string :university
      t.string :department
      t.string :year

      t.timestamps
    end
  end
end
