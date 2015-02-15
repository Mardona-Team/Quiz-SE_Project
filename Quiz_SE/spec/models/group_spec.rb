require 'rails_helper'

RSpec.describe Group, :type => :model do

  before(:each) do
    @user = create :user
    @group = create :group, instructor: @user
  end

  context 'attributes' do
    it "has group name" do
      @group.group_name = nil
      expect(@group).not_to be_valid
    end

    it "has subject" do
      @group.subject = nil
      expect(@group).not_to be_valid
    end

    it "has year" do
      @group.year = nil
      expect(@group).not_to be_valid
    end

    it "has unique group name" do
      another_group = create :group, instructor: @user
      @group.group_name = another_group.group_name
      expect(@group).not_to be_valid
    end

    it "has valid group name length" do
      @group.group_name = "foo"
      expect(@group).not_to be_valid
    end
  end

  context "relationships" do
    it "has an owner" do
      expect(@group).to be_valid
    end

    it "is not valid when no owner" do
      @group.instructor = nil
      expect(@group).not_to be_valid
    end
  end
end
