class Membership < ActiveRecord::Base

	belongs_to :student , class_name: 'User',foreign_key: 'student_id'
    belongs_to :group

    before_create :set_joined

    def set_joined
    	self.status = true
    end

end

